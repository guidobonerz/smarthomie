package de.drazil.homeautomation.smartdevices;

public class BasicRemoteMeteringSwitch extends BasicSmartDevice {
	public Double getCurrent() throws Throwable {
		return getValue(HmAttributes.CURRENT);
	}

	public Double getFrequency() throws Throwable {
		return getValue(HmAttributes.FREQUENCY);
	}

	public Double getVoltage() throws Throwable {
		return getValue(HmAttributes.VOLTAGE);
	}

	public Double getPower() throws Throwable {
		return getValue(HmAttributes.POWER);
	}

	public void setState(final Boolean state) throws Throwable {
		setValue(HmAttributes.STATE, state);
	}

	public void setState(final Integer channel, final Boolean state) throws Throwable {
		setValue(HmAttributes.STATE, state, channel);
	}

	public Boolean getState() throws Throwable {
		return getValue(HmAttributes.STATE);
	}

	public Boolean getState(final Integer channel) throws Throwable {
		return getValue(HmAttributes.STATE, channel);
	}

	public Integer getSignalStrength() throws Throwable {

		return getValue(HmAttributes.RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {

		return getValue(HmAttributes.LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {

		return getValue(HmAttributes.UNREACH);
	}
}
