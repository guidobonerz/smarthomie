package de.drazil.homeautomation.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Message {
	private String name;
	private Object payload;
}
