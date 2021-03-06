package de.drazil.homeautomation.smartdevices;

import de.drazil.homeautomation.util.VentilationCalcUtil;

public class BasicRemoteOutdoorWeatherSensor extends BasicSmartDevice
{

	public Integer getSignalStrength() throws Throwable
	{
		return getValue(HmAttributes.RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable
	{
		return getValue(HmAttributes.LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable
	{
		return getValue(HmAttributes.UNREACH);
	}

	public Number getCurrentTemperature() throws Throwable
	{
		return getValue(HmAttributes.TEMPERATURE);
	}

	public Integer getHumidity() throws Throwable
	{
		return getValue(HmAttributes.HUMIDITY);
	}

	public Double getBatteryValue() throws Throwable
	{
		throw new Throwable("function not supported by this device");
	}

	public Number getHumidityLevel() throws Throwable
	{
		return VentilationCalcUtil.getAbsoluteHumidity(getCurrentTemperature(), getHumidity());
	}
}
