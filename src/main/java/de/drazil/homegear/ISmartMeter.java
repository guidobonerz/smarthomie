package de.drazil.homegear;

public interface ISmartMeter {

	public Double getCurrent() throws Throwable;

	public Double getFrequency() throws Throwable;

	public Double getVoltage() throws Throwable;

	public Double getPower() throws Throwable;
}
