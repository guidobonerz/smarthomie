package de.drazil.homegear;

public class BasicRemoteMeteringSwitch extends BasicSmartDevice {
	public Double getCurrent() throws Throwable {
		return getValue("CURRENT");
	}

	public Double getFrequency() throws Throwable {
		return getValue("FREQUENCY");
	}

	public Double getVoltage() throws Throwable {
		return getValue("VOLTAGE");
	}

	public Double getPower() throws Throwable {
		return getValue("POWER");
	}

	public void setState(Boolean state) throws Throwable {
		setValue("STATE", state);
	}

	public Boolean getState() throws Throwable {
		return getValue("STATE");
	}

	public Integer getSignalStrength() throws Throwable {

		return getValue("RSSI_DEVICE");
	}

	public Boolean hasLowBattery() throws Throwable {

		return getValue("LOWBAT");
	}

	public Boolean isUnreachable() throws Throwable {

		return getValue("UNREACH");
	}
}
