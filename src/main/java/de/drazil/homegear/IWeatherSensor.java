package de.drazil.homegear;

public interface IWeatherSensor extends ITemperatureSensor, IHumiditySensor
{
	public Double getHumidityLevel() throws Throwable;
}
