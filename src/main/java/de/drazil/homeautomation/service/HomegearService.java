package de.drazil.homeautomation.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;
import de.drazil.homeautomation.smartdevices.IRemoteMeteringSwitch;
import de.drazil.homeautomation.smartdevices.IRemoteOutdoorWeatherSensor;
import de.drazil.homeautomation.smartdevices.IRemoteRadiatorThermostat;
import de.drazil.homeautomation.smartdevices.IRemoteSwitch;
import de.drazil.homeautomation.smartdevices.IRemoteValveDrive;
import de.drazil.homeautomation.smartdevices.IRemoteWallThermostat;
import de.drazil.homeautomation.smartdevices.ISmartDevice;
import de.drazil.homeautomation.smartdevices.IWeatherSensor;
import de.drazil.homeautomation.util.VentilationCalcUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomegearService {
	@Autowired
	HomegearDeviceService homegearDeviceService;

	public List<Map<String, Object>> getRemoteRadiatorThermostatList() throws Throwable {
		final List<Map<String, Object>> resultList = new ArrayList<>();

		final List<IRemoteRadiatorThermostat> list = homegearDeviceService
				.<IRemoteRadiatorThermostat>getSmartDeviceList(IRemoteRadiatorThermostat.class);
		for (final IRemoteRadiatorThermostat device : list) {

			final Map<String, Object> map = new LinkedHashMap<>();
			map.put("LowBattery", device.hasLowBattery());
			map.put("BatteryVoltage", device.getBatteryValue());
			map.put("SignalStrength", device.getSignalStrength());
			map.put("ControlMode", homegearDeviceService.getControlModeText(device.getControlMode()));
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("CurrentTemperature", device.getCurrentTemperature());
			map.put("DesiredTemperature", device.getDesiredTemperature());
			map.put("ValveState", device.getValveState());
			map.put("SerialNo", device.getSerialNo());
			resultList.add(map);

		}
		return resultList;
	}

	public List<Map<String, Object>> getRemoteWallThermostatList() throws Throwable {
		// old LEQ0567692
		final Number humidityLevelOut = homegearDeviceService.getRemoteOutdoorWeatherSensorBySerialNo("HEQ0237274")
				.getHumidityLevel();

		final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		final List<IRemoteWallThermostat> list = homegearDeviceService
				.<IRemoteWallThermostat>getSmartDeviceList(IRemoteWallThermostat.class);
		for (final IRemoteWallThermostat device : list) {
			final Number ct = device.getCurrentTemperature();
			final Number ch = device.getHumidity();
			final Number humidityLevelIn = VentilationCalcUtil.getAbsoluteHumidity(ct, ch);
			final Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("LowBattery", device.hasLowBattery());
			map.put("BatteryVoltage", device.getBatteryValue());
			map.put("SignalStrength", device.getSignalStrength());
			map.put("ControlMode", homegearDeviceService.getControlModeText(device.getControlMode()));
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("CurrentTemperature", ct);
			map.put("DesiredTemperature", device.getDesiredTemperature());
			map.put("Humidity", ch);
			map.put("SerialNo", device.getSerialNo());

			String key = device.getSerialNo();
			final String ventilationKey = key += ":Ventilation";
			final Object ventilation = System.getProperty(ventilationKey);
			final Number diff = humidityLevelOut.doubleValue() - humidityLevelIn.doubleValue();
			if (ventilation == null) {
				System.setProperty(ventilationKey, diff.doubleValue() < 0 ? "true" : "false");
			}

			if (diff.doubleValue() <= -0.3) {
				System.setProperty(ventilationKey, "true");
			} else if (diff.doubleValue() >= 0.3) {
				System.setProperty(ventilationKey, "false");
			}

			map.put("Ventilation", System.getProperty(ventilationKey).equals("true") ? true : false);
			map.put("VentilationDiff", diff);
			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> getRemoteOutdoorWeatherSensorList() throws Throwable {
		final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		final List<IRemoteOutdoorWeatherSensor> list = homegearDeviceService
				.<IRemoteOutdoorWeatherSensor>getSmartDeviceList(IRemoteOutdoorWeatherSensor.class);
		for (final IRemoteOutdoorWeatherSensor device : list) {
			final Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("LowBattery", device.hasLowBattery());
			map.put("SignalStrength", device.getSignalStrength());
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("CurrentTemperature", device.getCurrentTemperature());
			map.put("Humidity", device.getHumidity());
			map.put("SerialNo", device.getSerialNo());
			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> getWeatherSensorList() throws Throwable {
		final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		final List<IWeatherSensor> list = homegearDeviceService
				.<IWeatherSensor>getSmartDeviceList(IWeatherSensor.class);
		for (final IWeatherSensor device : list) {
			final Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("Location", device.getLocation());
			map.put("CurrentTemperature", device.getCurrentTemperature());
			map.put("Humidity", device.getHumidity());
			map.put("SerialNo", device.getSerialNo());
			if (device instanceof IRemoteWallThermostat) {
				map.put("DesiredTemperature", ((IRemoteWallThermostat) device).getDesiredTemperature());
			}
			map.put("Controller", (device instanceof IRemoteWallThermostat));
			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> getRemoteValveDriveList() throws Throwable {
		final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		final List<IRemoteValveDrive> list = homegearDeviceService
				.<IRemoteValveDrive>getSmartDeviceList(IRemoteValveDrive.class);
		for (final IRemoteValveDrive device : list) {
			final Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("Location", device.getLocation());
			map.put("SerialNo", device.getSerialNo());
			map.put("LowBattery", device.hasLowBattery());
			// map.put("BatteryVoltage", device.getBatteryValue());
			map.put("SignalStrength", device.getSignalStrength());
			map.put("ValveState", device.getValveState());
			map.put("Unreachable", device.isUnreachable());

			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> getRemoteMeteringSwitchList() throws Throwable {
		final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		final List<IRemoteMeteringSwitch> list = homegearDeviceService
				.<IRemoteMeteringSwitch>getSmartDeviceList(IRemoteMeteringSwitch.class);
		for (final IRemoteMeteringSwitch device : list) {
			final Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("SignalStrength", device.getSignalStrength());
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("Current", device.getCurrent());
			map.put("Power", device.getPower());
			map.put("Frequency", device.getFrequency());
			map.put("Voltage", device.getVoltage());
			map.put("State", device.getState());
			map.put("SerialNo", device.getSerialNo());
			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> getRemoteSwitchList() throws Throwable {
		final List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		final List<IRemoteSwitch> list = homegearDeviceService.<IRemoteSwitch>getSmartDeviceList(IRemoteSwitch.class);
		for (final IRemoteSwitch device : list) {
			final Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("SignalStrength", device.getSignalStrength());
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("State", device.getState());
			map.put("SerialNo", device.getSerialNo());
			resultList.add(map);
		}
		return resultList;
	}

	public Number getBoilerTemperature(final Integer channel) throws Throwable {
		return homegearDeviceService.getTemperatureDifferenceSensorBySerialNo("OEQ0676279").getTemperature(channel);
	}

	public void setBoilerState(final Integer channel, final boolean state) throws Throwable {
		homegearDeviceService.getRemoteSwitchBySerialNo("OEQ2070955").setState(channel, state);
	}

	public boolean getBoilerState(final Integer channel) throws Throwable {
		return homegearDeviceService.getRemoteSwitchBySerialNo("OEQ2070955").getState(channel);
	}

	public void setLight(final boolean state) throws Throwable {
		homegearDeviceService.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(state);
		homegearDeviceService.getRemoteSwitchBySerialNo("OEQ0479803").setState(state);
	}

	public void setHeating(final String state) throws Throwable {

		final List<IRemoteWallThermostat> list = homegearDeviceService.getSmartDeviceList(IRemoteWallThermostat.class);

		for (final IRemoteWallThermostat rt : list) {
			log.info(rt.getLocation());
			try {
				if (state.equalsIgnoreCase("auto")) {
					rt.setControlMode(HeatingMode.AUTO);
				} else if (state.equalsIgnoreCase("off")) {
					rt.setControlMode(HeatingMode.MANUAL, 0.0);
				}
			} catch (final Exception e) {
				log.error("error set heating", e);
			}
		}
	}

	public void setLight(final String location, final boolean state) throws Throwable {
		switch (location) {

		case "corridor": {
			homegearDeviceService.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(state);
			break;
		}
		case "livingroom": {
			homegearDeviceService.getRemoteSwitchBySerialNo("OEQ0479803").setState(state);
			break;
		}
		}
	}

	public <D extends ISmartDevice> D getSmartDevicebySerialNo(final String serialNo,
			final Class<? super D> deviceClass) throws Throwable {
		return homegearDeviceService.getSmartDeviceBySerialNo(serialNo, deviceClass);
	}
}
