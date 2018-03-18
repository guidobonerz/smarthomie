package de.drazil.homegear;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicRemoteRadiatorThermostat extends BasicRemoteThermostat {

	public Integer getValveState() throws Throwable {
		return getValue("VALVE_STATE");
	}

	public void setValveState(Integer value) throws Throwable {
		setValue("VALVE_STATE", value);
	}

	@Override
	public boolean isWallThermostat() {
		return false;
	}

	public void setBacklightDisabled(Boolean state) throws Throwable {
		Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put("BUTTON_RESPONSE_WITHOUT_BACKLIGHT", state);
		// because of a firmware error
		parameterSet.put("BACKLIGHT_ON_TIME", state ? 0 : 2);

		putParamset("CHANNEL0", "master", parameterSet);
	}

	public void setWindowState(Boolean state) throws Throwable {
		setValue("WINDOW_STATE", state);
	}
}
