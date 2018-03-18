package de.drazil.homegear;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicRemoteValveDrive extends BasicSmartDevice
{

	public void setValveState(Integer percentage) throws Throwable
	{
		setValue("VALVE_STATE", percentage);

		// Map<String, Object> parameterSet = new LinkedHashMap<>();
		// parameterSet.put("VALVE_STATE", new Integer(percentage));
		// putParamset("CHANNEL1", "master", parameterSet);

	}

	public Integer getValveState() throws Throwable
	{
		return getValue("VALVE_STATE");
	}

	public void setErrorValvePosition(Integer percentage) throws Throwable
	{
		Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put("VALVE_ERROR_POSITION", new Integer(percentage));
		putParamset("CHANNEL1", "master", parameterSet);
	}

	public Double getBatteryValue() throws Throwable
	{
		return getValue("BATTERY_STATE");
	}

	public Integer getSignalStrength() throws Throwable
	{

		return getValue("RSSI_DEVICE");
	}

	public Boolean hasLowBattery() throws Throwable
	{

		return getValue("LOWBAT");
	}

	public Boolean isUnreachable() throws Throwable
	{

		return getValue("UNREACH");
	}

	public void resetErrorState() throws Throwable
	{
		setValue("ERROR", 0);
	}

	public Integer getErrorState() throws Throwable
	{
		return getValue("ERROR");
	}
}
