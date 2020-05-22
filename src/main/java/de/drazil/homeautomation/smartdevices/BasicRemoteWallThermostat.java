package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;
import de.drazil.homeautomation.util.VentilationCalcUtil;

public class BasicRemoteWallThermostat extends BasicRemoteThermostat {

	public Integer getHumidity() throws Throwable {
		return getValue(HUMIDITY);
	}

	@Override
	public boolean isWallThermostat() {
		return true;
	}

	public void setWeekProgram(final WeekProgram weekProgram) throws Throwable {
		setWeekProgram(weekProgram, false);
	}

	public void setWeekProgram(final WeekProgram weekProgram, final boolean activateProfile) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(WEEK_PROGRAM_POINTER, weekProgram.getName());
		putParamset(CHANNEL0, "master", parameterSet);

		if (activateProfile) {
			setControlMode(HeatingMode.AUTO);
		}
	}

	public void setWakeOnRadioEnabled(final Boolean state) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(BURST_RX, state);
		putParamset(CHANNEL0, "master", parameterSet);
	}

	public Number getHumidityLevel() throws Throwable {
		return VentilationCalcUtil.getAbsoluteHumidity(getCurrentTemperature(), getHumidity());
	}

	public void setWindowState(final Boolean state) throws Throwable {
		setValue(WINDOW_STATE, state);
	}
}
