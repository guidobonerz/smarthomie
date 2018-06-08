package de.drazil.homeautomation.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class DynamicEvent {
	private String key;
	private Date start;
}
