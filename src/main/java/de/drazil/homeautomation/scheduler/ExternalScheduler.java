package de.drazil.homeautomation.scheduler;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import com.jayway.jsonpath.JsonPath;

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
import de.drazil.homeautomation.service.HomegearService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExternalScheduler {

	@Autowired
	HomegearService homegearService;

	@Autowired
	private ExternalSchedulerService service;
	@Autowired
	TaskScheduler taskScheduler;

	@Value("${app.scheduler-enabled}")
	private boolean schedulerEnabled;

	@Value("${app.timezone}")
	private String timezone;

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private Runnable controlLight(String location, boolean state) {
		return () -> {
			try {
				homegearService.setLight(location, state);
			} catch (final Throwable e) {
				log.error("could not switch on {} light {}", location, (state ? "on" : "off"));
			}
		};
	}

	private Runnable controlBoiler(boolean state, Double temperature) {
		return () -> {
			try {
				homegearService.setTemperture(temperature);
				homegearService.setBoilerState(1, state);
				log.info("start boiler heating to {} ", temperature);
			} catch (final Throwable e) {
				log.error("could not switch boiler heating {}", (state ? "on" : "off"));
			}
		};
	}

	@PostConstruct
	public void init() {
		/*
		 * final GregorianCalendar dateTime = new GregorianCalendar();
		 * 
		 * AzimuthZenithAngle position = SPA.calculateSolarPosition(dateTime, 51.56838,
		 * // latitude (degrees) 6.72703, // longitude (degrees) 30, // elevation (m)
		 * DeltaT.estimate(dateTime)); System.out.println("SPA: " + position);
		 */
		readDailyEvents();
		importYearlyEvents();
	}

	private void addSchedule(String dateTime, Runnable r) {
		final LocalDateTime ldt = LocalDateTime.parse(dateTime, dateTimeFormatter);
		taskScheduler.schedule(r, ZonedDateTime.of(ldt, ZoneId.of(timezone)).toInstant());
	}

	private void buildScheduler() {
		final List<Event> eventList = service.getEventList();
		for (final Event event : eventList) {
			event.setStartRule(getPatchedRule(event.getStartRule()));
			event.setEndRule(getPatchedRule(event.getEndRule()));
			if (event.getDescription().equals("FloorLamp")) {
				addSchedule(event.getStartRule(), controlLight("corridor", true));
				addSchedule(event.getEndRule(), controlLight("corridor", false));
			} else if (event.getDescription().equals("LivingRoomLamp")) {
				addSchedule(event.getStartRule(), controlLight("livingroom", true));
				addSchedule(event.getEndRule(), controlLight("livingroom", false));
			} else if (event.getDescription().equals("Boiler")) {
				if (event.getStartRule() != null) {
					addSchedule(event.getStartRule(), controlBoiler(true, Double.valueOf(event.getPayload())));
				}
			}
		}
	}

	private String getPatchedRule(final String rule) {
		String patchedRule = rule;
		if (rule != null) {
			if (rule.startsWith(":SUNRISE")) {
				final DynamicEvent de = service.getDynamicEventById("sunrise");
				patchedRule = LocalDateTime.parse(de.getTargetDate(), dateTimeFormatter).format(dateTimeFormatter);
			} else if (rule.startsWith(":SUNSET")) {
				final DynamicEvent de = service.getDynamicEventById("sunset");
				patchedRule = LocalDateTime.parse(de.getTargetDate(), dateTimeFormatter).minusHours(1)
						.format(dateTimeFormatter);
			} else if (rule.startsWith(":TODAY")) {
				patchedRule = patchedRule.replace(":TODAY", LocalDate.now().format(dateFormatter));
			} else if (rule.startsWith(":TOMORROW")) {
				patchedRule = patchedRule.replace(":TOMORROW", LocalDate.now().plusDays(1).format(dateFormatter));
			} else if (rule.startsWith(":WORKDAY")) {
				LocalDate date = LocalDate.now();
				if (Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
						DayOfWeek.FRIDAY).contains(date.getDayOfWeek())) {
					patchedRule = patchedRule.replace(":WORKDAY", date.format(dateFormatter));
				} else {
					patchedRule = null;
				}
			} else if (rule.startsWith(":WEEKEND")) {
				LocalDate date = LocalDate.now();
				if (Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(date.getDayOfWeek())) {
					patchedRule = patchedRule.replace(":WEEKEND", date.format(dateFormatter));
				} else {
					patchedRule = null;
				}
			} else if (rule.startsWith(":BOOST")) {
				LocalDate date = LocalDate.now();
				if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
					patchedRule = patchedRule.replace(":BOOST", date.format(dateFormatter));
				} else {
					patchedRule = null;
				}
			}
		}
		return patchedRule;
	}

	@Scheduled(cron = "0 0 0 * * *")
	private void readDailyEvents() {

		final String sunrise = service.readDataFromUrl("https://api.sunrise-sunset.org/json?lat=51.56838&lng=6.72703");
		if (sunrise != null) {
			final LinkedHashMap<String, ?> sunsetMap = JsonPath.parse(sunrise).read("$.results");
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			for (final String id : sunsetMap.keySet()) {
				final String value = (String) sunsetMap.get(id);
				if (!id.equals("day_length")) {
					final LocalTime lt = LocalTime.parse(value, DateTimeFormatter.ofPattern("h:m:s a"));
					LocalDateTime ldt = LocalDate.now().atTime(lt);

					final ZonedDateTime utcTimeZoned = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
					ldt = utcTimeZoned.withZoneSameInstant(ZoneId.of("CET")).toLocalDateTime();

					final boolean isDST = utcTimeZoned.getZone().getRules()
							.isDaylightSavings(ldt.toInstant(utcTimeZoned.getOffset()));
					ldt = ldt.plusHours(isDST ? 1 : 0);
					service.addOrUpdateDynamicEvent(id, ldt.format(formatter));
				}
			}
			buildScheduler();
		} else {
			log.error("can't get sunrise/set from remote server");
		}
	}

	@Scheduled(cron = "0 0 0 1 1 *")
	private void importYearlyEvents() {

		final LocalDate localDate = LocalDate.now();
		final int year = localDate.getYear();

		// wastecalender
		// ---------------------------------------------------------------------
		String groupId = "Müllkalender" + year;
		if (!service.checkGroup(groupId)) {
			final String result = service.readDataFromUrl(
					"https://www.dinslaken.de/www/web_io.nsf/index.xsp?rule=neu&path=%2Fsys%2Fdienstleistungslayout-abfallservice-ausgabe-2%2F&Bezirk=Rilkeweg&AS_Rest=14+t%C3%A4gige+Leerung");
			if (result != null) {
				addICalEvents(groupId, result);
			} else {
				log.error("can't get wastecalender from remote server");
			}
		}
		// holidays
		// ---------------------------------------------------------------------

		groupId = "Ferien" + year;
		if (!service.checkGroup(groupId)) {
			final String result = service.readDataFromUrl(
					String.format("https://www.ferienwiki.de/exports/ferien/%s/de/nordrhein-westfalen", year));
			if (result != null) {
				addICalEvents(groupId, result);
			} else {
				log.error("can't get school vacations from remote server");
			}
		}

		// feiertage
		// ---------------------------------------------------------------------
		groupId = "Feiertage" + year;
		if (!service.checkGroup(groupId)) {

			String url = String.format("http://de-kalender.de/downloads/feiertage_nordrhein-westfalen_%s_et.ics", year);
			final String result = service.readDataFromUrl(url);
			if (result != null) {
				addICalEvents(groupId, result);
			} else {
				log.error("can't get bankholidays from remote server");
			}
		}

		// feiertage
		// ---------------------------------------------------------------------
		groupId = "Kalenderwochen" + year;
		if (!service.checkGroup(groupId)) {

			String url = String.format("http://de-kalender.de/downloads/kalenderwochen_%s.ics", year);
			final String result = service.readDataFromUrl(url);
			if (result != null) {
				addICalEvents(groupId, result);
			} else {
				log.error("can't get calendar weeks from remote server");
			}
		}
	}

	private void addICalEvents(String groupId, String iCalResult) {
		final List<ICalendar> icals = Biweekly.parse(iCalResult).all();
		for (final ICalendar ic : icals) {
			for (final VEvent event : ic.getEvents()) {
				final String description = event.getSummary().getValue();
				service.addEvent(groupId, new Date(event.getDateStart().getValue().getTime()).toString() + " 00:00:00",
						new Date(event.getDateEnd().getValue().getTime()).toString() + " 00:00:00", description, true,
						service.getWasteCatgory(description), -1);
			}
		}
	}
}
