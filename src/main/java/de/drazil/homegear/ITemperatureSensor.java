package de.drazil.homegear;

public interface ITemperatureSensor extends ISmartDevice {
	public Number getCurrentTemperature() throws Throwable;
}
