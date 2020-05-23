package de.drazil.homeautomation.smartdevicesimpl.homematic;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homeautomation.smartdevices.BasicRemoteWallThermostat;
import de.drazil.homeautomation.smartdevices.HmAttributes;
import de.drazil.homeautomation.smartdevices.IRemoteWallThermostat;

public class HomematicRemoteWallThermostat extends BasicRemoteWallThermostat implements IRemoteWallThermostat {
	public void toggleTemperatureAndHumidity(final boolean toggle) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(HmAttributes.SHOW_HUMIDITY.getName(), toggle ? 1 : 0);
		putParamset(HmAttributes.CHANNEL0, "master", parameterSet);
	}
}
