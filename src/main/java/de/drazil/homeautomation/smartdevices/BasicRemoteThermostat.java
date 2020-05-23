package de.drazil.homeautomation.smartdevices;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homeautomation.bean.HeatingPhase;
import de.drazil.homeautomation.bean.HeatingProfile;
import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;

public abstract class BasicRemoteThermostat extends BasicSmartDevice {
	public Number getBatteryValue() throws Throwable {
		return getValue(HmAttributes.BATTERY_STATE);
	}

	public Integer getControlMode() throws Throwable {

		return getValue(HmAttributes.CONTROL_MODE);
	}

	public void setControlMode(final HeatingMode mode) throws Throwable {
		setControlMode(mode, 18.0);
	}

	public void setControlMode(final HeatingMode mode, final Double temperature) throws Throwable {
		switch (mode) {
			case AUTO:
				setValue(HmAttributes.AUTO_MODE, Boolean.TRUE);
				break;
			case MANUAL:
				setValue(HmAttributes.MANU_MODE, temperature);
				break;
			case PARTY:
				setValue(HmAttributes.AUTO_MODE, Boolean.TRUE);
				break;
			case BOOST:
				setValue(HmAttributes.BOOST_MODE, Boolean.TRUE);
				break;
		}

	}

	public Number getCurrentTemperature() throws Throwable {
		return getValue(HmAttributes.ACTUAL_TEMPERATURE);
	}

	public Number getDesiredTemperature() throws Throwable {
		return getValue(HmAttributes.SET_TEMPERATURE);
	}

	public Integer getSignalStrength() throws Throwable {
		return getValue(HmAttributes.RSSI_DEVICE);
	}

	public Boolean hasLowBattery() throws Throwable {
		return getValue(HmAttributes.LOWBAT);
	}

	public Boolean isUnreachable() throws Throwable {
		return getValue(HmAttributes.UNREACH);
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
						prefix = HmAttributes.PREFIX_P1.getName();
						break;
					case WEEK_PROGRAM_2:
						prefix = HmAttributes.PREFIX_P2.getName();
						break;
					case WEEK_PROGRAM_3:
						prefix = HmAttributes.PREFIX_P3.getName();
						break;
				}
				parameterSet.put(HmAttributes.WEEK_PROGRAM_POINTER.getName(), weekProgram.getName());
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

					parameterSet.put(prefix + HmAttributes.STARTTIME_PART.getName() + day.getName() + "_" + i,
							startTimeMinutes);
					parameterSet.put(prefix + HmAttributes.ENDTIME_PART.getName() + day.getName() + "_" + i,
							endTimeMinutes);
					parameterSet.put(prefix + HmAttributes.TEMPERATURE_PART.getName() + day.getName() + "_" + i,
							phase.getDesiredTemperature());
					if (lastPhase != null) {
						parameterSet.put(prefix + HmAttributes.ENDTIME_PART.getName() + day.getName() + "_" + (i - 1),
								startTimeMinutes);
					}
					lastPhase = phase;
					i++;
				}
			}

			putParamset(HmAttributes.CHANNEL0, "master", parameterSet);

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
		parameterSet.put(global ? HmAttributes.GLOBAL_BUTTON_LOCK.getName() : HmAttributes.BUTTON_LOCK.getName(),
				state);
		putParamset(HmAttributes.CHANNEL0, "master", parameterSet);
	}

	public void setWakeOnRadioEnabled(final Boolean state) throws Throwable {
		final Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(HmAttributes.BURST_RX.getName(), state);
		putParamset(HmAttributes.CHANNEL0, "master", parameterSet);
	}
}
