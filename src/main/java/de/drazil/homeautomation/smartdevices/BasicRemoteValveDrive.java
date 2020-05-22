package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicRemoteValveDrive extends BasicSmartDevice {

	public void setValveState(final Integer percentage) throws Throwable {
		setValue(VALVE_STATE, percentage);

		// Map<String, Object> parameterSet = new LinkedHashMap<>();
		// parameterSet.put("VALVE_STATE", new Integer(percentage));
		// putParamset("CHANNEL1", "master", parameterSet);

	}

	public Integer getValveState() throws Throwable {
		return getValue(VALVE_STATE);
	}

	public void setErrorValvePosition(final Integer percentage) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(VALVE_ERROR_POSITION, percentage);
		putParamset(CHANNEL1, "master", parameterSet);
	}

	public Double getBatteryValue() throws Throwable {
		return getValue(BATTERY_STATE);
	}

	public Integer getSignalStrength() throws Throwable {

		return getValue(RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {

		return getValue(LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {

		return getValue(UNREACH);
	}

	public void resetErrorState() throws Throwable {
		setValue(ERROR, 0);
	}

	public Integer getErrorState() throws Throwable {
		return getValue(ERROR);
	}
}
