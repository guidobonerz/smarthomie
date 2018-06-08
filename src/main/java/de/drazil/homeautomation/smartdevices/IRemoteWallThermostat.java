package de.drazil.homeautomation.smartdevices;

public interface IRemoteWallThermostat extends IBatteryPowered, IRemoteDevice, IHeatingDevice, IWeatherSensor {
	public void setWeekProgram(WeekProgram weekProgram) throws Throwable;

	public void setWeekProgram(WeekProgram weekProgram, boolean activateProfile) throws Throwable;

	public void setWindowState(Boolean state) throws Throwable;
}
