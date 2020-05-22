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

	protected <ValueType> ValueType getValue(final String valueName) throws Exception {
		return null;
	}

	protected <ValueType> void setValue(final String valueName, final ValueType value) throws Exception
	{

	}
}
