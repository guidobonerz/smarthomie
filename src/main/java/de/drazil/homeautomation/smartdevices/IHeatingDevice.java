package de.drazil.homeautomation.smartdevices;

import de.drazil.homeautomation.bean.HeatingProfile;

public interface IHeatingDevice extends ITemperatureSensor {
	public enum HeatingMode {
		AUTO, MANUAL, BOOST, PARTY
	}

	public Number getDesiredTemperature() throws Throwable;

	public Integer getControlMode() throws Throwable;

	public void setControlMode(HeatingMode mode, Double temperature) throws Throwable;

	public void setControlMode(HeatingMode mode) throws Throwable;

	public void setHeatingProfile(HeatingProfile... profile) throws Throwable;

	public void setHeatingProfile(WeekProgram weekProgram, boolean activateProfile, HeatingProfile... profile)
			throws Throwable;

	public Boolean isLocked(Boolean global) throws Throwable;

	public void setLocked(Boolean state) throws Throwable;

	public void setLocked(Boolean state, Boolean global) throws Throwable;

	public void setWakeOnRadioEnabled(Boolean state) throws Throwable;
}
