package de.drazil.homeautomation.smartdevices;

public interface IBatteryPowered extends ISmartDevice
{
	public Boolean hasLowBattery() throws Throwable;

	public Number getBatteryValue() throws Throwable;
}
