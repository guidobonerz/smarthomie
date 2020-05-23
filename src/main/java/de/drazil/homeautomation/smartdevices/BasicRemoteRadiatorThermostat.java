package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicRemoteRadiatorThermostat extends BasicRemoteThermostat {

	public Integer getValveState() throws Throwable {
		return getValue(HmAttributes.VALVE_STATE);
	}

	public void setValveState(final Integer value) throws Throwable {
		setValue(HmAttributes.VALVE_STATE, value);
	}

	@Override
	public boolean isWallThermostat() {
		return false;
	}

	public void setBacklightDisabled(final Boolean state) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(HmAttributes.BUTTON_RESPONSE_WITHOUT_BACKLIGHT.getName(), state);
		// because of a firmware error
		parameterSet.put(HmAttributes.BACKLIGHT_ON_TIME.getName(), state ? 0 : 2);

		putParamset(HmAttributes.CHANNEL0, "master", parameterSet);
	}

	public void setWindowState(final Boolean state) throws Throwable {
		setValue(HmAttributes.WINDOW_STATE, state);
	}
}
