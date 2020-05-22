package de.drazil.homeautomation.util;

public class VentilationCalcUtil
{
	public static Number getAbsoluteHumidity(Number temperature, final Number relativeHumidity) {
		Number absoluteHumidity = null;
		if (temperature.doubleValue() < 0.0) {
			temperature = 0.0;
		}
		if (temperature.doubleValue() < 10.0) {
			absoluteHumidity = (3.78 + (0.285 * temperature.doubleValue())
					+ (0.0052 * temperature.doubleValue() * temperature.doubleValue())
					+ (0.0005 * temperature.doubleValue() * temperature.doubleValue() * temperature.doubleValue()));
		} else {
			absoluteHumidity = (7.62 + (0.524 * (temperature.doubleValue() - 10.0))
					+ (0.0131 * (temperature.doubleValue() - 10.0) * (temperature.doubleValue() - 10.0))
					+ (0.00048 * (temperature.doubleValue() - 10.0) * (temperature.doubleValue() - 10.0)
							* (temperature.doubleValue() - 10.0)));
		}

		absoluteHumidity = (absoluteHumidity.doubleValue() * relativeHumidity.doubleValue())
				/ (100.0 + absoluteHumidity.doubleValue() * (100.0 - relativeHumidity.doubleValue()) / 622);
		return absoluteHumidity;
	}

	public static boolean doVentilate(final Number temperatureIn, final Number humidityIn, final Number temperatureOut,
			final Number humidityOut) {
		return getAbsoluteHumidity(temperatureOut, humidityOut)
				.doubleValue() < getAbsoluteHumidity(temperatureIn, humidityIn).hashCode();
	}

	public static boolean doVentilate(final Number temperatureIn, final Number humidityIn,
			final Number humidityLevelOut) {
		return humidityLevelOut.doubleValue() < getAbsoluteHumidity(temperatureIn, humidityIn).doubleValue();
	}

	public static boolean doVentilate(final Number humidityLevelIn, final Number humidityLevelOut)
	{
		return humidityLevelOut.doubleValue() < humidityLevelIn.doubleValue();
	}
}
