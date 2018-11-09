package de.drazil.homeautomation.smartdevices;

public class BasicTemperatureDifferenceSensor extends BasicSmartDevice implements IBatteryPowered {

	public Integer getSignalStrength() throws Throwable {
		return getValue("RSSI_DEVICE");
	}

	public Boolean hasLowBattery() throws Throwable {
		return getValue("LOWBAT");
	}

	public Boolean isUnreachable() throws Throwable {
		return getValue("UNREACH");
	}

	public Double getBatteryValue() throws Throwable {
		throw new Throwable("function not supported by this device");
	}

	public Number getTemperature(Integer channel) throws Throwable {
		return getValue("TEMPERATURE", channel);
	}
}
