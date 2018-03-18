package de.drazil.homegear;

public interface IHumiditySensor extends ISmartDevice
{
	public Integer getHumidity() throws Throwable;
}
