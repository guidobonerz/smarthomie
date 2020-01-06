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

@Service
public class ExternalSchedulerService {
	private static final Logger log = LoggerFactory.getLogger(ExternalSchedulerService.class);
	@Autowired
	private ExternalSchedulerDao dao;

	public boolean checkGroup(String groupId) {
		return dao.checkGroup(groupId);
	}

	public void removeEventByCalenderName(String calenderName) {
		dao.removeEventByCalenderName(calenderName);
	}

	public void addEvent(String groupId, String start_rule, String end_rule, String description, boolean allDayEvent,
			int categoryId, int actionId) {
		dao.addEvent(groupId, start_rule, end_rule, description, allDayEvent, categoryId, actionId);
	}

	public void addOrUpdateDynamicEvent(String id, String target_date) {
		dao.addOrUpdateDynamicEvent(id, target_date);
	}

	public List<Event> getEventList() {
		return dao.getEventList();
	}

	public DynamicEvent getDynamicEventById(String id) {
		return dao.getDynamicEventById(id);
	}

	public List<Event> getUpcomingEventList(int from, int to) {
		return dao.getUpcomingEventList(from, to);
	}

	public List<Event> getUpcomingEventList(String events[]) {
		return dao.getUpcomingEventList(events);
	}

	public List<Event> getUpcomingEventActionList(String events[]) {
		return dao.getUpcomingEventActionList(events);
	}

	public String readDataFromUrl(String url) {

		String result = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);
			int errorCode = response.getStatusLine().getStatusCode();
			if (errorCode ==200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}
			// log.info(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Timestamp getJavaTimestampFromUnixTimestamp(String epochTime) {
		long epoch = Double.valueOf(epochTime).longValue();
		Timestamp date = new Timestamp(epoch * 1000);
		return date;
	}

	public Date getJavaDateFromUnixTimestamp(String epochTime) {
		long epoch = Double.valueOf(epochTime).longValue();
		Date date = new Date(epoch * 1000);
		return date;
	}

	public Time getJavaTimeFromUnixTimestamp(String epochTime) {
		long epoch = Double.valueOf(epochTime).longValue();
		Time date = new Time(epoch * 1000);
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
