package de.drazil.homeautomation.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.drazil.homeautomation.dao.ExternalSchedulerDao;
import de.drazil.homeautomation.dto.DynamicEvent;
import de.drazil.homeautomation.dto.Event;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalSchedulerService {
	private static final Logger log = LoggerFactory.getLogger(ExternalSchedulerService.class);
	@Autowired
	private ExternalSchedulerDao dao;

	public boolean checkGroup(final String groupId) {
		return dao.checkGroup(groupId);
	}

	public void removeEventByCalenderName(final String calenderName) {
		dao.removeEventByCalenderName(calenderName);
	}

	public void addEvent(final String groupId, final String start_rule, final String end_rule, final String description,
			final boolean allDayEvent, final int categoryId, final int actionId) {
		dao.addEvent(groupId, start_rule, end_rule, description, allDayEvent, categoryId, actionId);
	}

	public void addOrUpdateDynamicEvent(final String id, final String target_date) {
		dao.addOrUpdateDynamicEvent(id, target_date);
	}

	public List<Event> getEventList() {
		return dao.getEventList();
	}

	public DynamicEvent getDynamicEventById(final String id) {
		return dao.getDynamicEventById(id);
	}

	public List<Event> getUpcomingEventList(final int from, final int to) {
		return dao.getUpcomingEventList(from, to);
	}

	public List<Event> getUpcomingEventList(final String events[]) {
		return dao.getUpcomingEventList(events);
	}

	public List<Event> getUpcomingEventActionList(final String events[]) {
		return dao.getUpcomingEventActionList(events);
	}

	public String readDataFromUrl(final String url) {

		String result = null;
		final HttpClient client = HttpClientBuilder.create().build();

		final HttpGet request = new HttpGet(url);

		request.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
		try {
			final HttpResponse response = client.execute(request);
			final int errorCode = response.getStatusLine().getStatusCode();
			if (errorCode == 200) {
				final HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}

		} catch (final IOException e) {
			log.error("DOWNLOADERROR:", e);
			e.printStackTrace();
		}
		return result;
	}

	public Timestamp getJavaTimestampFromUnixTimestamp(final String epochTime) {
		final long epoch = Double.valueOf(epochTime).longValue();
		final Timestamp date = new Timestamp(epoch * 1000);
		return date;
	}

	public Date getJavaDateFromUnixTimestamp(final String epochTime) {
		final long epoch = Double.valueOf(epochTime).longValue();
		final Date date = new Date(epoch * 1000);
		return date;
	}

	public Time getJavaTimeFromUnixTimestamp(final String epochTime) {
		final long epoch = Double.valueOf(epochTime).longValue();
		final Time date = new Time(epoch * 1000);
		return date;
	}

	public int getWasteCatgory(String description) {
		int categoryId = -1;
		description = description.toLowerCase();
		if (description.startsWith("bio")) {
			categoryId = 5;
		} else if (description.startsWith("rest")) {
			categoryId = 4;
		}
		if (description.startsWith("sperr")) {
			categoryId = 10;
		}
		if (description.startsWith("gelb")) {
			categoryId = 6;
		}
		if (description.startsWith("alt")) {
			categoryId = 14;
		}
		return categoryId;
	}
}
