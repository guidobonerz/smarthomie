package de.drazil.homegear;

public interface IOneWireDevice
{
	public String getSerialNo();

	public String getLocation();

	public String getFamily();

	public String getType();

	public boolean isExternallyPowered();

}
