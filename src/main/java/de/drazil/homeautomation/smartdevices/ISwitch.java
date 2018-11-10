package de.drazil.homeautomation.smartdevices;

public interface ISwitch {
	public void setState(Boolean state) throws Throwable;

	public void setState(Integer channel, Boolean state) throws Throwable;

	public Boolean getState() throws Throwable;

	public Boolean getState(Integer channel) throws Throwable;
}
