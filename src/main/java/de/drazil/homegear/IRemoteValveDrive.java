package de.drazil.homegear;

public interface IRemoteValveDrive extends IBatteryPowered, IRemoteDevice
{
	public void setValveState(Integer percentage) throws Throwable;

	public void setErrorValvePosition(Integer percentage) throws Throwable;

	public Integer getValveState() throws Throwable;

	public Integer getErrorState() throws Throwable;

	public void resetErrorState() throws Throwable;

}
