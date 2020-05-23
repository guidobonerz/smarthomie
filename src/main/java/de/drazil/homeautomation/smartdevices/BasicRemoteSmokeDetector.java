package de.drazil.homeautomation.smartdevices;

public class BasicRemoteSmokeDetector extends BasicSmartDevice {
	public Integer getSignalStrength() throws Throwable {
		return getValue(HmAttributes.RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {
		return getValue(HmAttributes.LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {
		return getValue(HmAttributes.UNREACH);
	}

	public Boolean getState() throws Throwable {
		return getValue(HmAttributes.STATE);
	}

	public Boolean hasSmokeChamberError() throws Throwable {
		return getValue(HmAttributes.ERROR_SMOKE_CHAMBER);
	}

	public Boolean hasAlarmTestError() throws Throwable {
		return getValue(HmAttributes.ERROR_ALARM_TEST);
	}

	public Boolean getInstallTest() throws Throwable {
		return getValue(HmAttributes.INSTALL_TEST);
	}

}
