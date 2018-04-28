package de.drazil.homegear;

import de.drazil.homeautomation.service.HomegearDeviceService;

public interface ISmartDevice {
	public void setSerialNo(String serialNo);

	public String getSerialNo();

	public String getLocation() throws Throwable;

	public void setHomegearDeviceFactory(HomegearDeviceService factory);
}
