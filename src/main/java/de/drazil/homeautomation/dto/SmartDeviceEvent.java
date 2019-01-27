package de.drazil.homeautomation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SmartDeviceEvent {
	String location;
	String serialNo;
	String interfaceId;
	int peerId;
	int channel;
	String parameterName;
	Object value;
}
