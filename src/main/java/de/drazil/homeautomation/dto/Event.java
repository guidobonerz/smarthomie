package de.drazil.homeautomation.dto;

import lombok.Data;

@Data
public class Event {
	private String groupId;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String description;
	private boolean allDayEvent;
	private long categoryId;
	private long actionId;
	private long diff;
	private String event;

}
