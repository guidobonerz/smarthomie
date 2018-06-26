package de.drazil.homeautomation.smartdevices;

public interface IRemoteDevice extends ISmartDevice
{
	public Integer getSignalStrength() throws Throwable;

	public Boolean isUnreachable() throws Throwable;

}
