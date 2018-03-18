package de.drazil.homegear;

import de.drazil.homegear.util.VentilationCalcUtil;

public class BasicRemoteOutdoorWeatherSensor extends BasicSmartDevice
{

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

	public Number getCurrentTemperature() throws Throwable
	{
		return getValue("TEMPERATURE");
	}

	public Integer getHumidity() throws Throwable
	{
		return getValue("HUMIDITY");
	}

	public Double getBatteryValue() throws Throwable
	{
		throw new Throwable("function not supported by this device");
	}

	public Double getHumidityLevel() throws Throwable
	{
		return VentilationCalcUtil.getAbsoluteHumidity(getCurrentTemperature(), getHumidity());
	}
}
