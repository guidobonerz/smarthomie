package de.drazil.homeautomation.smartdevices;

public class BasicOneWireDevice
{
	public String getSerialNo()
	{
		return null;
	}

	public String getLocation()
	{
		return null;
	}

	public String getFamily()
	{
		return null;
	}

	public String getType()
	{
		return null;
	}

	public boolean isExternallyPowered()
	{
		return true;
	}

	protected <ValueType> ValueType getValue(String valueName) throws Exception
	{
		return null;
	}

	protected <ValueType> void setValue(String valueName, ValueType value) throws Exception
	{

	}
}
