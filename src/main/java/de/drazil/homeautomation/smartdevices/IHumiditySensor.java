package de.drazil.homeautomation.smartdevices;

public interface IHumiditySensor extends ISmartDevice
{
	public Integer getHumidity() throws Throwable;
}
