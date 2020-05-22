package de.drazil.homeautomation.smartdevices;

public class BasicRemoteSmokeDetector extends BasicSmartDevice {
	public Integer getSignalStrength() throws Throwable {
		return getValue(RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {
		return getValue(LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {
		return getValue(UNREACH);
	}

	public Boolean getState() throws Throwable {
		return getValue(STATE);
	}

	public Boolean hasSmokeChamberError() throws Throwable {
		return getValue(ERROR_SMOKE_CHAMBER);
	}

	public Boolean hasAlarmTestError() throws Throwable {
		return getValue(ERROR_ALARM_TEST);
	}

	public Boolean getInstallTest() throws Throwable {
		return getValue(INSTALL_TEST);
	}

}
