package de.drazil.homeautomation.smartdevices;

import de.drazil.homeautomation.service.HomegearDeviceService;

public interface ISmartDevice {
	public void setSerialNo(String serialNo);

	public String getSerialNo();

	public String getLocation() throws Throwable;

	public void setHomegearDeviceService(HomegearDeviceService service);
}
