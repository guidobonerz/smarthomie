package de.drazil.homegear;

import java.util.LinkedHashMap;
import java.util.Map;

import de.drazil.homegear.IHeatingDevice.HeatingMode;
import de.drazil.homegear.bean.HeatingPhase;
import de.drazil.homegear.bean.HeatingProfile;

public abstract class BasicRemoteThermostat extends BasicSmartDevice {
	public Double getBatteryValue() throws Throwable {
		return getValue("BATTERY_STATE");
	}

	public Integer getControlMode() throws Throwable {

		return getValue("CONTROL_MODE");
	}

	public void setControlMode(HeatingMode mode) throws Throwable {
		setControlMode(mode, 18.0);
	}

	public void setControlMode(HeatingMode mode, Double temperature) throws Throwable {
		switch (mode) {
		case AUTO:
			setValue("AUTO_MODE", new Boolean(true));
			break;
		case MANUAL:
			setValue("MANU_MODE", temperature);
			break;
		case PARTY:
			setValue("AUTO_MODE", new Boolean(true));
			break;
		case BOOST:
			setValue("BOOST_MODE", new Boolean(true));
			break;
		}

	}

	public Number getCurrentTemperature() throws Throwable {
		return getValue("ACTUAL_TEMPERATURE");
	}

	public Number getDesiredTemperature() throws Throwable {
		return getValue("SET_TEMPERATURE");
	}

	public Integer getSignalStrength() throws Throwable {
		return getValue("RSSI_DEVICE");
	}

	public Boolean hasLowBattery() throws Throwable {
		return getValue("LOWBAT");
	}

	public Boolean isUnreachable() throws Throwable {
		return getValue("UNREACH");
	}

 	public void setHeatingProfile(HeatingProfile... profiles) throws Throwable {
		setHeatingProfile(WeekProgram.WEEK_PROGRAM_1, true, profiles);
	}

	public void setHeatingProfile(WeekProgram weekProgram, boolean activateProfile, HeatingProfile... profiles)
			throws Throwable {
		Map<String, Object> parameterSet = new LinkedHashMap<>();

		for (HeatingProfile profile : profiles) {

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
					prefix = "P1_";
					break;
				case WEEK_PROGRAM_2:
					prefix = "P2_";
					break;
				case WEEK_PROGRAM_3:
					prefix = "P3_";
					break;
				}
				parameterSet.put("WEEK_PROGRAM_POINTER", weekProgram.getName());
			}

			for (Day day : days) {
				int i = 1;
				HeatingPhase lastPhase = null;

				for (HeatingPhase phase : profile.getHeatingPhases()) {
					String startTime = phase.getStartTime();
					String startTimeArray[] = startTime.split(":");

					int startTimeMinutes = (Integer.valueOf(startTimeArray[0]) * 60)
							+ Integer.valueOf(startTimeArray[1]);
					int endTimeMinutes = 1440; // 24:00

					parameterSet.put(prefix + "STARTTIME_" + day.getName() + "_" + i, startTimeMinutes);
					parameterSet.put(prefix + "ENDTIME_" + day.getName() + "_" + i, endTimeMinutes);
					parameterSet.put(prefix + "TEMPERATURE_" + day.getName() + "_" + i, phase.getDesiredTemperature());
					if (lastPhase != null) {
						parameterSet.put(prefix + "ENDTIME_" + day.getName() + "_" + (i - 1), startTimeMinutes);
					}
					lastPhase = phase;
					i++;
				}
			}

			putParamset("CHANNEL0", "master", parameterSet);

			if (activateProfile) {
				setControlMode(HeatingMode.AUTO);
			}
		}
	}

	public abstract boolean isWallThermostat();

	public Boolean isLocked(Boolean global) throws Throwable {
		return false;
	}

	public void setLocked(Boolean state) throws Throwable {
		setLocked(state, true);
	}

	public void setLocked(Boolean state, Boolean global) throws Throwable {
		Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put(global ? "GLOBAL_BUTTON_LOCK" : "BUTTON_LOCK", state);
		putParamset("CHANNEL0", "master", parameterSet);
	}

	public void setWakeOnRadioEnabled(Boolean state) throws Throwable {
		Map<String, Object> parameterSet = new LinkedHashMap<>();
		parameterSet.put("BURST_RX", state);
		putParamset("CHANNEL0", "master", parameterSet);
	}
}
