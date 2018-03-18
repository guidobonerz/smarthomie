package de.drazil.homegear;

public interface IBatteryPowered extends ISmartDevice
{
	public Boolean hasLowBattery() throws Throwable;

	public Double getBatteryValue() throws Throwable;
}
