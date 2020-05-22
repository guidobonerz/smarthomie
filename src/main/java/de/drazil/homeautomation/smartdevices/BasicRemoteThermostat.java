package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homeautomation.bean.HeatingPhase;
import de.drazil.homeautomation.bean.HeatingProfile;
import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;

public abstract class BasicRemoteThermostat extends BasicSmartDevice {
	public Number getBatteryValue() throws Throwable {
		return getValue(BATTERY_STATE);
	}

	public Integer getControlMode() throws Throwable {

		return getValue(CONTROL_MODE);
	}

	public void setControlMode(final HeatingMode mode) throws Throwable {
		setControlMode(mode, 18.0);
	}

	public void setControlMode(final HeatingMode mode, final Double temperature) throws Throwable {
		switch (mode) {
			case AUTO:
				setValue(AUTO_MODE, Boolean.TRUE);
				break;
			case MANUAL:
				setValue(MANU_MODE, temperature);
				break;
			case PARTY:
				setValue(AUTO_MODE, Boolean.TRUE);
				break;
			case BOOST:
				setValue(BOOST_MODE, Boolean.TRUE);
				break;
		}

	}

	public Number getCurrentTemperature() throws Throwable {
		return getValue(ACTUAL_TEMPERATURE);
	}

	public Number getDesiredTemperature() throws Throwable {
		return getValue(SET_TEMPERATURE);
	}

	public Integer getSignalStrength() throws Throwable {
		return getValue(RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {
		return getValue(LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {
		return getValue(UNREACH);
	}

	public void setHeatingProfile(final HeatingProfile... profiles) throws Throwable {
		setHeatingProfile(WeekProgram.WEEK_PROGRAM_1, true, profiles);
	}

	public void setHeatingProfile(final WeekProgram weekProgram, final boolean activateProfile,
			final HeatingProfile... profiles) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();

		for (final HeatingProfile profile : profiles) {

			if (profile == null || profile.getHeatingPhases().size() == 0)
				return;
			if (profile.getHeatingPhases().size() > 13)
				throw new Exception("only 13 heating phases allowed.");

			Day days[] = profile.getDays();

			if (days.length == 1 && days[0].equals(Day.ALL_DAYS)) {
				days = Day.getAllDays();
			} else if (days.length == 1 && days[0].equals(Day.WORK_DAYS)) {
				days = Day.getWorkDays();
			} else if (days.length == 1 && days[0].equals(Day.WEEKEND_DAYS)) {
				days = Day.getWeekendDays();
			}

			String prefix = "";

			if (isWallThermostat()) {
				switch (weekProgram) {
					case WEEK_PROGRAM_1:
						prefix = PREFIX_P1;
						break;
					case WEEK_PROGRAM_2:
						prefix = PREFIX_P2;
						break;
					case WEEK_PROGRAM_3:
						prefix = PREFIX_P3;
						break;
				}
				parameterSet.put(WEEK_PROGRAM_POINTER, weekProgram.getName());
			}

			for (final Day day : days) {
				int i = 1;
				HeatingPhase lastPhase = null;

				for (final HeatingPhase phase : profile.getHeatingPhases()) {
					final String startTime = phase.getStartTime();
					final String startTimeArray[] = startTime.split(":");

					final int startTimeMinutes = (Integer.valueOf(startTimeArray[0]) * 60)
							+ Integer.valueOf(startTimeArray[1]);
					final int endTimeMinutes = 1440; // 24:00

					parameterSet.put(prefix + STARTTIME_PART + day.getName() + "_" + i, startTimeMinutes);
					parameterSet.put(prefix + ENDTIME_PART + day.getName() + "_" + i, endTimeMinutes);
					parameterSet.put(prefix + TEMPERATURE_PART + day.getName() + "_" + i,
							phase.getDesiredTemperature());
					if (lastPhase != null) {
						parameterSet.put(prefix + ENDTIME_PART + day.getName() + "_" + (i - 1), startTimeMinutes);
					}
					lastPhase = phase;
					i++;
				}
			}

			putParamset(CHANNEL0, "master", parameterSet);

			if (activateProfile) {
				setControlMode(HeatingMode.AUTO);
			}
		}
	}

	public abstract boolean isWallThermostat();

	public Boolean isLocked(final Boolean global) throws Throwable {
		return false;
	}

	public void setLocked(final Boolean state) throws Throwable {
		setLocked(state, true);
	}

	public void setLocked(final Boolean state, final Boolean global) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(global ? GLOBAL_BUTTON_LOCK : BUTTON_LOCK, state);
		putParamset(CHANNEL0, "master", parameterSet);
	}

	public void setWakeOnRadioEnabled(final Boolean state) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(BURST_RX, state);
		putParamset(CHANNEL0, "master", parameterSet);
	}
}
