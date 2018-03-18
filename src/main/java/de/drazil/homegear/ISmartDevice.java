package de.drazil.homegear;

public interface ISmartDevice {
	public void setSerialNo(String serialNo);

	public String getSerialNo();

	public String getLocation() throws Throwable;

	public void setHomegearDeviceFactory(HomegearDeviceFactory factory);
}
