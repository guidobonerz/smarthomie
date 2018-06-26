package de.drazil.homeautomation.smartdevicesimpl.homematic;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homeautomation.smartdevices.BasicRemoteWallThermostat;
import de.drazil.homeautomation.smartdevices.IRemoteWallThermostat;

public class HomematicRemoteWallThermostat extends BasicRemoteWallThermostat implements IRemoteWallThermostat {
	public void toggleTemperatureAndHumidity(boolean toggle) throws Throwable {
		Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put("SHOW_HUMIDITY", toggle ? 1 : 0);
		putParamset("CHANNEL0", "master", parameterSet);
	}
}
