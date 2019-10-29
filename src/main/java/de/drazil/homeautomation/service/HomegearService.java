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

@Service
public class HomegearService {
	@Autowired
	HomegearDeviceService homegearDeviceService;

	public List<Map<String, Object>> getRemoteRadiatorThermostatList() throws Throwable {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteRadiatorThermostat> list = homegearDeviceService
				.<IRemoteRadiatorThermostat>getSmartDeviceList(IRemoteRadiatorThermostat.class);
		for (IRemoteRadiatorThermostat device : list) {

			Map<String, Object> map = new LinkedHashMap<String, Object>();
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
		//old LEQ0567692
		Number humidityLevelOut = homegearDeviceService.getRemoteOutdoorWeatherSensorBySerialNo("HEQ0237274")
				.getHumidityLevel();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteWallThermostat> list = homegearDeviceService
				.<IRemoteWallThermostat>getSmartDeviceList(IRemoteWallThermostat.class);
		for (IRemoteWallThermostat device : list) {
			Number ct = device.getCurrentTemperature();
			Number ch = device.getHumidity();
			Number humidityLevelIn = VentilationCalcUtil.getAbsoluteHumidity(ct, ch);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
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
			String ventilationKey = key += ":Ventilation";
			Object ventilation = System.getProperty(ventilationKey);
			Number diff = humidityLevelOut.doubleValue() - humidityLevelIn.doubleValue();
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
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		List<IRemoteOutdoorWeatherSensor> list = homegearDeviceService
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

		List<IWeatherSensor> list = homegearDeviceService.<IWeatherSensor>getSmartDeviceList(IWeatherSensor.class);
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

		List<IRemoteValveDrive> list = homegearDeviceService
				.<IRemoteValveDrive>getSmartDeviceList(IRemoteValveDrive.class);
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

		List<IRemoteMeteringSwitch> list = homegearDeviceService
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

		List<IRemoteSwitch> list = homegearDeviceService.<IRemoteSwitch>getSmartDeviceList(IRemoteSwitch.class);
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

	/*
	 * public void setBoiler(boolean state) throws Throwable {
	 * homegearDeviceService.getRemoteValveDriveBySerialNo("HEQ0134004").
	 * setValveState(state ? 70 : 0); }
	 */
	public Number getBoilerTemperature(Integer channel) throws Throwable {
		return homegearDeviceService.getTemperatureDifferenceSensorBySerialNo("OEQ0676279").getTemperature(channel);
	}

	public void setBoilerState(Integer channel, boolean state) throws Throwable {
		homegearDeviceService.getRemoteSwitchBySerialNo("OEQ2070955").setState(channel, state);
	}

	public boolean getBoilerState(Integer channel) throws Throwable {
		return homegearDeviceService.getRemoteSwitchBySerialNo("OEQ2070955").getState(channel);
	}

	public void setLight(boolean state) throws Throwable {
		homegearDeviceService.getRemoteMeteringSwitchBySerialNo("LEQ0531814").setState(state);
		homegearDeviceService.getRemoteSwitchBySerialNo("OEQ0479803").setState(state);
	}

	public void setHeating(String state) throws Throwable {

		List<IRemoteWallThermostat> list = homegearDeviceService.getSmartDeviceList(IRemoteWallThermostat.class);

		for (IRemoteWallThermostat rt : list) {
			System.out.println(rt.getLocation());
			try {
				if (state.equalsIgnoreCase("auto")) {
					rt.setControlMode(HeatingMode.AUTO);
				} else if (state.equalsIgnoreCase("off")) {
					rt.setControlMode(HeatingMode.MANUAL, new Double(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setLight(String location, boolean state) throws Throwable {
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

	public <D extends ISmartDevice> D getSmartDevicebySerialNo(String serialNo, Class<? super D> deviceClass)
			throws Throwable {
		return homegearDeviceService.getSmartDeviceBySerialNo(serialNo, deviceClass);
	}
}
