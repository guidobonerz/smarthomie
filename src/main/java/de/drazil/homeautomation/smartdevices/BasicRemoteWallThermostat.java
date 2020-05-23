package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;
import de.drazil.homeautomation.util.VentilationCalcUtil;

public class BasicRemoteWallThermostat extends BasicRemoteThermostat {

	public Integer getHumidity() throws Throwable {
		return getValue(HmAttributes.HUMIDITY);
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
		parameterSet.put(HmAttributes.WEEK_PROGRAM_POINTER.getName(), weekProgram.getName());
		putParamset(HmAttributes.CHANNEL0, "master", parameterSet);

		if (activateProfile) {
			setControlMode(HeatingMode.AUTO);
		}
	}

	public void setWakeOnRadioEnabled(final Boolean state) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(HmAttributes.BURST_RX.getName(), state);
		putParamset(HmAttributes.CHANNEL0, "master", parameterSet);
	}

	public Number getHumidityLevel() throws Throwable {
		return VentilationCalcUtil.getAbsoluteHumidity(getCurrentTemperature(), getHumidity());
	}

	public void setWindowState(final Boolean state) throws Throwable {
		setValue(HmAttributes.WINDOW_STATE, state);
	}
}
