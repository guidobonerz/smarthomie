package de.drazil.homeautomation.smartdevices;

public interface ISwitch {
	public void setState(Boolean state) throws Throwable;

	public Boolean getState() throws Throwable;
}
