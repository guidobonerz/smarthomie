package de.drazil.homeautomation.scheduler;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import de.drazil.homeautomation.dto.DynamicEvent;
import de.drazil.homeautomation.dto.Event;
import de.drazil.homeautomation.service.ExternalSchedulerService;
import de.drazil.homeautomation.service.HomegearDeviceService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExternalScheduler {

	@Autowired
	HomegearDeviceService homegearDeviceService;
	@Autowired
	private ExternalSchedulerService service;
	@Autowired
	TaskScheduler taskScheduler;

	@Value("${app.scheduler-enabled}")
	private boolean schedulerEnabled;

	@Value("${app.timezone}")
	private String timezone;

	String groupId;

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private Runnable floorlampOn() {
		return () -> {
			try {
				homegearDeviceService.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(true);
			} catch (Throwable e) {
				log.error("could not switch on floor lamp");
			}
		};
	}

	private Runnable floorlampOff() {
		return () -> {
			try {
				homegearDeviceService.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(false);
			} catch (Throwable e) {
				log.error("could not switch off floor lamp");
			}
		};
	}

	private Runnable livingroolLampOn() {
		return () -> {
			try {
				homegearDeviceService.getRemoteSwitchBySerialNo("OEQ0479803").setState(true);
			} catch (Throwable e) {
				log.error("could not switch on livingroom lamp");
			}
		};
	}

	private Runnable livingroolLampOff() {
		return () -> {
			try {
				homegearDeviceService.getRemoteSwitchBySerialNo("OEQ0479803").setState(false);
			} catch (Throwable e) {
				log.error("could not switch off livingroom lamp");
			}
		};
	}

	@PostConstruct
	public void init() {
		readDailyEvents();
		readYearlyEvents();
	}

	private void buildScheduler() {
		List<Event> eventList = service.getEventList();
		for (Event event : eventList) {
			processEvent(event);
			if (event.getDescription().equals("FloorLamp")) {
				LocalDateTime start = LocalDateTime.parse(event.getStartRule(), dateTimeFormatter);
				LocalDateTime end = LocalDateTime.parse(event.getEndRule(), dateTimeFormatter);
				taskScheduler.schedule(floorlampOn(), ZonedDateTime.of(start, ZoneId.of(timezone)).toInstant());
				taskScheduler.schedule(floorlampOff(), ZonedDateTime.of(end, ZoneId.of(timezone)).toInstant());
			} else if (event.getDescription().equals("LivingRoomLamp")) {
				LocalDateTime start = LocalDateTime.parse(event.getStartRule(), dateTimeFormatter);
				LocalDateTime end = LocalDateTime.parse(event.getEndRule(), dateTimeFormatter);
				taskScheduler.schedule(livingroolLampOn(), ZonedDateTime.of(start, ZoneId.of(timezone)).toInstant());
				taskScheduler.schedule(livingroolLampOff(), ZonedDateTime.of(end, ZoneId.of(timezone)).toInstant());
			}
		}
	}

	private void processEvent(Event event) {
		if (event.getStartRule() != null) {
			event.setStartRule(getPatchedRule(event.getStartRule()));
		}
		if (event.getEndRule() != null) {
			event.setEndRule(getPatchedRule(event.getEndRule()));
		}
	}

	private String getPatchedRule(String rule) {
		String patchedRule = rule;
		DynamicEvent de = service.getDynamicEventById(rule);

		if (de != null) {
			patchedRule = LocalDateTime.parse(de.getTargetDate(), dateTimeFormatter).format(dateTimeFormatter);
		} else if (rule.startsWith("{TODAY}")) {
			patchedRule = patchedRule.replace("{TODAY}", LocalDate.now().format(dateFormatter));
		} else if (rule.startsWith("{TOMORROW}")) {
			patchedRule = patchedRule.replace("{TODAY}", LocalDate.now().plusDays(1).format(dateFormatter));
		}

		return patchedRule;
	}

	@Scheduled(cron = "0 0 0 * * *")
	private void readDailyEvents() {
		// openweathermap
		// ---------------------------------------------------------------------
		// String openweathermap =
		// service.readCalender("http://api.openweathermap.org/data/2.5/weather?q=Dinslaken,de&units=metric&APPID=cce438ee0049647cc63adb6598fd65c4");
		// sunrise/set
		// ---------------------------------------------------------------------
		String sunrise = service.readDataFromUrl("https://api.sunrise-sunset.org/json?lat=51.56838&lng=6.72703");
		if (sunrise != null) {
			LinkedHashMap<String, ?> sunsetMap = JsonPath.parse(sunrise).read("$.results");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			for (String id : sunsetMap.keySet()) {
				String value = (String) sunsetMap.get(id);
				if (!id.equals("day_length")) {
					LocalTime lt = LocalTime.parse(value, DateTimeFormatter.ofPattern("h:m:s a"));
					// LocalTime lt = LocalTime.parse(value, DateTimeFormatter.ofPattern("H:m:s"));
					LocalDateTime ldt = LocalDate.now().atTime(lt);

					ZonedDateTime utcTimeZoned = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
					ldt = utcTimeZoned.withZoneSameInstant(ZoneId.of("CET")).toLocalDateTime();

					boolean isDST = utcTimeZoned.getZone().getRules()
							.isDaylightSavings(ldt.toInstant(utcTimeZoned.getOffset()));
					ldt = ldt.plusHours(isDST ? 1 : 0);
					service.addOrUpdateDynamicEvent(id, ldt.format(formatter));
				}
			}

			// darksky weather
			// ---------------------------------------------------------------------
			// String darkSkyWeather =
			// service.readDataFromUrl("https://api.darksky.net/forecast/22f4b40c5eebd547bf007fb1bd247287/51.56838,%206.72703?lang=de&units=auto");
			// LinkedHashMap<String, ?> currently =
			// JsonPath.parse(darkSkyWeather).read("$.currently");
			// LinkedHashMap<String, ?> hourly =
			// JsonPath.parse(darkSkyWeather).read("$.hourly");
			// LinkedHashMap<String, ?> daily =
			// JsonPath.parse(darkSkyWeather).read("$.daily");
			// Object alters = JsonPath.parse(darkSkyWeather).read("$.alerts");
			buildScheduler();
		} else {
			log.error("can't get sunrise/set from remote server");
		}

	}

	@Scheduled(cron = "0 0 0 1 1 *")
	private void readYearlyEvents() {

		LocalDate localDate = LocalDate.now();
		int year = localDate.getYear();

		TypeRef<List<LinkedHashMap<Object, String>>> typeRef = new TypeRef<List<LinkedHashMap<Object, String>>>() {
		};

		Configuration.setDefaults(new Configuration.Defaults() {

			private final JsonProvider jsonProvider = new JacksonJsonProvider();
			private final MappingProvider mappingProvider = new JacksonMappingProvider();

			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}

			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			@Override
			public Set<Option> options() {
				return EnumSet.noneOf(Option.class);
			}
		});

		// wastecalender
		// ---------------------------------------------------------------------
		groupId = "Müllkalender" + year;
		if (!service.checkGroup(groupId)) {
			String waste = service.readDataFromUrl(
					"https://www.dinslaken.de/www/web_io.nsf/index.xsp?rule=neu&path=%2Fsys%2Fdienstleistungslayout-abfallservice-ausgabe-2%2F&Bezirk=Rilkeweg&AS_Rest=14+t%C3%A4gige+Leerung");
			if (waste != null) {
				List<ICalendar> icals = Biweekly.parse(waste).all();
				for (ICalendar ic : icals) {
					for (VEvent event : ic.getEvents()) {
						String description = event.getSummary().getValue();
						service.addEvent(groupId,
								new Date(event.getDateStart().getValue().getTime()).toString() + " 00:00:00",
								new Date(event.getDateEnd().getValue().getTime()).toString() + " 00:00:00", description,
								true, service.getWasteCatgory(description), -1);
					}
				}
			} else {
				log.error("can't get wastecalender from remote server");
			}
		}
		// holidays
		// ---------------------------------------------------------------------
		groupId = "Ferien" + year;
		if (!service.checkGroup(groupId)) {
			String schoolVacation = service.readDataFromUrl("https://ferien-api.de/api/v1/holidays/NW/" + year);
			if (schoolVacation != null) {
				List<LinkedHashMap<Object, String>> schoolVacationList = JsonPath.parse(schoolVacation).read("$.*",
						typeRef);
				for (LinkedHashMap<Object, String> map : schoolVacationList) {
					String start = map.get("start");
					String end = map.get("end");
					service.addEvent(groupId, LocalDateTime.parse(start).toString(),
							LocalDateTime.parse(end).toString(), map.get("name"), true, 13, -1);
				}
			} else {
				log.error("can't get schulferien from remote server");
			}
		}
		// feiertage
		// ---------------------------------------------------------------------
		/*
		 * groupId = "Feiertage" + year; if (!service.checkGroup(groupId)) { String
		 * bankHolidays =
		 * service.readDataFromUrl("https://feiertage-api.de/api/?nur_land=nw&jahr=" +
		 * year); if (bankHolidays != null) { List<LinkedHashMap<Object, String>>
		 * bankHolidayList = JsonPath.parse(bankHolidays).read("$.*", typeRef); for
		 * (LinkedHashMap<Object, String> map : bankHolidayList) { String start =
		 * map.get("datum"); service.addEvent(groupId,
		 * LocalDate.parse(start).toString(), LocalDate.parse(start).toString(),
		 * map.get("name"), true, 11, -1); } } else {
		 * log.error("can't get feiertage from remote server"); } }
		 */

	}
}
