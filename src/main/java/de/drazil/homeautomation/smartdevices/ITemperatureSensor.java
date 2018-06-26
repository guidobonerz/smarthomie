package de.drazil.homeautomation.smartdevices;

public interface ITemperatureSensor extends ISmartDevice {
	public Number getCurrentTemperature() throws Throwable;
}
