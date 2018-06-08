package de.drazil.homeautomation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeatingPhase {
	private String startTime;
	private Double desiredTemperature;
}
