package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicRemoteValveDrive extends BasicSmartDevice {

	public void setValveState(final Integer percentage) throws Throwable {
		setValue(HmAttributes.VALVE_STATE, percentage);

		// Map<String, Object> parameterSet = new LinkedHashMap<>();
		// parameterSet.put("VALVE_STATE", new Integer(percentage));
		// putParamset("CHANNEL1", "master", parameterSet);

	}

	public Integer getValveState() throws Throwable {
		return getValue(HmAttributes.VALVE_STATE);
	}

	public void setErrorValvePosition(final Integer percentage) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(HmAttributes.VALVE_ERROR_POSITION.getName(), percentage);
		putParamset(HmAttributes.CHANNEL1, "master", parameterSet);
	}

	public Double getBatteryValue() throws Throwable {
		return getValue(HmAttributes.BATTERY_STATE);
	}

	public Integer getSignalStrength() throws Throwable {

		return getValue(HmAttributes.RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {

		return getValue(HmAttributes.LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {

		return getValue(HmAttributes.UNREACH);
	}

	public void resetErrorState() throws Throwable {
		setValue(HmAttributes.ERROR, 0);
	}

	public Integer getErrorState() throws Throwable {
		return getValue(HmAttributes.ERROR);
	}
}
