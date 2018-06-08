package de.drazil.homeautomation.scheduler;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import de.drazil.homeautomation.service.ExternalSchedulerService;

@Component
public class ExternalScheduler {
	private static final Logger log = LoggerFactory.getLogger(ExternalScheduler.class);

	@Autowired
	private ExternalSchedulerService service;

	@Value("${app.scheduler-enabled}")
	private boolean schedulerEnabled;

	@Autowired
	TaskScheduler taskScheduler;
	ScheduledFuture<?> scheduledFuture;

	private Runnable sunrise() {
		return () -> log.info("sunrise");
	}

	void sunset() {
		log.info("sunset");
	}

	@Scheduled(initialDelay = 5000, fixedDelay = 500000) // , cron = "00 16 23 * * ?")
	// @Scheduled(cron = "*/10 * * * * *")
	public void readUpcomingEvents() {

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

		// scheduledFuture = taskScheduler.schedule(sunrise(), new CronTrigger("*/5 * *
		// * * *"));
		// openweathermap
		// ---------------------------------------------------------------------
		// String openweathermap =
		// service.readCalender("http://api.openweathermap.org/data/2.5/weather?q=Dinslaken,de&units=metric&APPID=cce438ee0049647cc63adb6598fd65c4");
		// sunrise/set
		// ---------------------------------------------------------------------
		if (schedulerEnabled) {
			String sunrise = service.readDataFromUrl("https://api.sunrise-sunset.org/json?lat=51.5674264&lng=6.747534");
			LinkedHashMap<String, ?> sunsetMap = JsonPath.parse(sunrise).read("$.results");

			for (String id : sunsetMap.keySet()) {
				String value = (String) sunsetMap.get(id);
				if (!id.equals("day_length")) {
					LocalTime lt = LocalTime.parse(value, DateTimeFormatter.ofPattern("h:m:s a"));
					Date date = Date.valueOf(LocalDate.now().plusDays(1));
					service.addOrUpdateDynamicEvent(id, date.toString(), lt.toString());
				} else {
					Date date = Date.valueOf(LocalDate.now());
					LocalTime lt = LocalTime.parse(value, DateTimeFormatter.ofPattern("H:m:s"));
					service.addOrUpdateDynamicEvent(id, date.toString(), lt.toString());
				}
			}

			// wastecalender
			// ---------------------------------------------------------------------
			String waste = service.readDataFromUrl(
					"https://www.dinslaken.de/www/web_io.nsf/index.xsp?rule=neu&path=%2Fsys%2Fdienstleistungslayout-abfallservice-ausgabe-2%2F&Bezirk=Rilkeweg&AS_Rest=14+t%C3%A4gige+Leerung");
			service.removeEventByCalenderName("Müllkalender2018");
			List<ICalendar> icals = Biweekly.parse(waste).all();
			for (ICalendar ic : icals) {
				for (VEvent event : ic.getEvents()) {
					String description = event.getSummary().getValue();
					service.addEvent("Müllkalender2018", new Date(event.getDateStart().getValue().getTime()).toString(),
							"00:00:00", new Date(event.getDateEnd().getValue().getTime()).toString(), "00:00:00",
							description, true, service.getWasteCatgory(description), -1);
				}
			}
			// holidays
			// ---------------------------------------------------------------------
			// String schoolVacation =
			// service.readCalender("https://ferien-api.de/api/v1/holidays/NW/2018");
			String schoolVacation = service
					.readDataFromUrl("http://api.smartnoob.de/ferien/v1/ferien/?bundesland=nw&jahr=2018");
			service.removeEventByCalenderName("Ferien2018");

			List<LinkedHashMap<Object, String>> schoolVacationList = JsonPath.parse(schoolVacation).read("$.daten.*",
					typeRef);
			for (LinkedHashMap<Object, String> map : schoolVacationList) {
				String start = map.get("beginn");
				String end = map.get("ende");
				service.addEvent("Ferien2018", service.getJavaDateFromUnixTimestamp(start).toString(),
						service.getJavaTimeFromUnixTimestamp(start).toString(),
						service.getJavaDateFromUnixTimestamp(end).toString(),
						service.getJavaTimeFromUnixTimestamp(end).toString(), map.get("title"), true, 13, -1);
			}
			// feiertage
			// ---------------------------------------------------------------------
			// String bankHolidays =
			// service.readCalender("https://feiertage-api.de/api/?jahr=2018&nur_land=NW");
			String bankHolidays = service
					.readDataFromUrl("http://api.smartnoob.de/ferien/v1/feiertage/?bundesland=nw&jahr=2018");
			service.removeEventByCalenderName("Feiertage2018");

			List<LinkedHashMap<Object, String>> bankHolidayList = JsonPath.parse(bankHolidays).read("$.daten.*",
					typeRef);
			for (LinkedHashMap<Object, String> map : bankHolidayList) {
				String start = map.get("beginn");
				String end = map.get("ende");
				service.addEvent("Feiertage2018", service.getJavaDateFromUnixTimestamp(start).toString(),
						service.getJavaTimeFromUnixTimestamp(start).toString(),
						service.getJavaDateFromUnixTimestamp(end).toString(),
						service.getJavaTimeFromUnixTimestamp(end).toString(), map.get("title"), true, 11, -1);
			}

			// darksky weather
			// ---------------------------------------------------------------------
			String darkSkyWeather = service.readDataFromUrl(
					"https://api.darksky.net/forecast/22f4b40c5eebd547bf007fb1bd247287/51.566735,%206.745985?lang=de&units=auto");
			LinkedHashMap<String, ?> currently = JsonPath.parse(darkSkyWeather).read("$.currently");
			LinkedHashMap<String, ?> hourly = JsonPath.parse(darkSkyWeather).read("$.hourly");
			LinkedHashMap<String, ?> daily = JsonPath.parse(darkSkyWeather).read("$.daily");
			// Object alters = JsonPath.parse(darkSkyWeather).read("$.alerts");

		}

	}

}
