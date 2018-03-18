package de.drazil.homegear;

public interface IRemoteRadiatorThermostat extends IBatteryPowered, IRemoteDevice, IHeatingDevice {
	public Integer getValveState() throws Throwable;

	public void setValveState(Integer value) throws Throwable;

	public void setBacklightDisabled(Boolean state) throws Throwable;

	public void setWindowState(Boolean state) throws Throwable;
}
