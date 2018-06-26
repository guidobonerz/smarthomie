package de.drazil.homeautomation.smartdevices;

public interface IWeatherSensor extends ITemperatureSensor, IHumiditySensor
{
	public Number getHumidityLevel() throws Throwable;
}
