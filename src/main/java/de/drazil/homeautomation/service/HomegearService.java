package de.drazil.homeautomation.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.drazil.homegear.IRemoteMeteringSwitch;
import de.drazil.homegear.IRemoteOutdoorWeatherSensor;
import de.drazil.homegear.IRemoteRadiatorThermostat;
import de.drazil.homegear.IRemoteSwitch;
import de.drazil.homegear.IRemoteValveDrive;
import de.drazil.homegear.IRemoteWallThermostat;
import de.drazil.homegear.ISmartDevice;
import de.drazil.homegear.IWeatherSensor;
import de.drazil.homegear.util.VentilationCalcUtil;

@Service
public class HomegearService {
	@Autowired
	HomegearDeviceService factory;

	public List<Map<String, Object>> getRemoteRadiatorThermostatList() throws Throwable {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteRadiatorThermostat> list = factory
				.<IRemoteRadiatorThermostat>getSmartDeviceList(IRemoteRadiatorThermostat.class);
		for (IRemoteRadiatorThermostat device : list) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("LowBattery", device.hasLowBattery());
			map.put("BatteryVoltage", device.getBatteryValue());
			map.put("SignalStrength", device.getSignalStrength());
			map.put("ControlMode", factory.getControlModeText(device.getControlMode()));
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
		Double humidityLevelOut = factory.getRemoteOutdoorWeatherSensorBySerialNo("LEQ0567692").getHumidityLevel();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteWallThermostat> list = factory
				.<IRemoteWallThermostat>getSmartDeviceList(IRemoteWallThermostat.class);
		for (IRemoteWallThermostat device : list) {
			Number ct = device.getCurrentTemperature();
			Integer ch = device.getHumidity();
			Double humidityLevelIn = VentilationCalcUtil.getAbsoluteHumidity(ct, ch);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("LowBattery", device.hasLowBattery());
			map.put("BatteryVoltage", device.getBatteryValue());
			map.put("SignalStrength", device.getSignalStrength());
			map.put("ControlMode", factory.getControlModeText(device.getControlMode()));
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("CurrentTemperature", ct);
			map.put("DesiredTemperature", device.getDesiredTemperature());
			map.put("Humidity", ch);
			map.put("SerialNo", device.getSerialNo());

			String key = device.getSerialNo();
			String ventilationKey = key += ":Ventilation";
			Object ventilation = System.getProperty(ventilationKey);
			Double diff = humidityLevelOut - humidityLevelIn;
			if (ventilation == null) {
				System.setProperty(ventilationKey, diff < 0 ? "true" : "false");
			}

			if (diff <= -0.3) {
				System.setProperty(ventilationKey, "true");
			} else if (diff >= 0.3) {
				System.setProperty(ventilationKey, "false");
			}

			map.put("Ventilation", System.getProperty(ventilationKey).equals("true") ? true : false);
			map.put("VentilationDiff", diff);
			resultList.add(map);
		}
		return resultList;
	}

	public List<Map<String, Object>> getRemoteOutdoorWeatherSensorList() throws Throwable {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteOutdoorWeatherSensor> list = factory
				.<IRemoteOutdoorWeatherSensor>getSmartDeviceList(IRemoteOutdoorWeatherSensor.class);
		for (IRemoteOutdoorWeatherSensor device : list) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
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
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IWeatherSensor> list = factory.<IWeatherSensor>getSmartDeviceList(IWeatherSensor.class);
		for (IWeatherSensor device : list) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
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
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteValveDrive> list = factory.<IRemoteValveDrive>getSmartDeviceList(IRemoteValveDrive.class);
		for (IRemoteValveDrive device : list) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
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
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteMeteringSwitch> list = factory
				.<IRemoteMeteringSwitch>getSmartDeviceList(IRemoteMeteringSwitch.class);
		for (IRemoteMeteringSwitch device : list) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
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
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteSwitch> list = factory.<IRemoteSwitch>getSmartDeviceList(IRemoteSwitch.class);
		for (IRemoteSwitch device : list) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("SignalStrength", device.getSignalStrength());
			map.put("Unreachable", device.isUnreachable());
			map.put("Location", device.getLocation());
			map.put("State", device.getState());
			map.put("SerialNo", device.getSerialNo());
			resultList.add(map);
		}
		return resultList;
	}

	public void setBoiler(boolean state) throws Throwable {
		factory.getRemoteValveDriveBySerialNo("HEQ0134004").setValveState(state ? 70 : 0);
	}

	public void setLight(boolean state) throws Throwable {
		factory.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(state);
		factory.getRemoteSwitchBySerialNo("OEQ0479803").setState(state);
	}

	public void setLight(String location, boolean state) throws Throwable {
		switch (location) {
		case "livingroom": {
			factory.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(state);
		}
		case "corrider": {
			factory.getRemoteSwitchBySerialNo("OEQ0479803").setState(state);
		}
		}
	}

	public <D extends ISmartDevice> D getSmartDevicebySerialNo(String serialNo, Class<? super D> deviceClass)
			throws Throwable {
		return factory.getSmartDeviceBySerialNo(serialNo, deviceClass);
	}
}
