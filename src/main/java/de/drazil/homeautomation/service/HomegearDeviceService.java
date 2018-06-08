package de.drazil.homeautomation.service;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import de.drazil.homeautomation.bean.Device;
import de.drazil.homeautomation.bean.DeviceConfig;
import de.drazil.homeautomation.bean.DeviceId;
import de.drazil.homeautomation.smartdevices.IRemoteMeteringSwitch;
import de.drazil.homeautomation.smartdevices.IRemoteOutdoorWeatherSensor;
import de.drazil.homeautomation.smartdevices.IRemoteRadiatorThermostat;
import de.drazil.homeautomation.smartdevices.IRemoteSmokeDetector;
import de.drazil.homeautomation.smartdevices.IRemoteSwitch;
import de.drazil.homeautomation.smartdevices.IRemoteValveDrive;
import de.drazil.homeautomation.smartdevices.IRemoteWallThermostat;
import de.drazil.homeautomation.smartdevices.ISmartDevice;
import de.drazil.homeautomation.xml.XmlHandler;

@Service
public class HomegearDeviceService {

	private static final Logger Log = Logger.getLogger(HomegearDeviceService.class.getSimpleName());
	private JsonRpcHttpClient rpcClient = null;
	private DeviceConfig devices = null;
	private Map<String, DeviceId> deviceSerialMap;
	private Map<String, Device> deviceMap;

	@Value("${homegear.host}")
	private String homegearHost;
	@Value("${homegear.port}")
	private String homegearPort;
	@Value("${homegear.xmlrpc.server.host}")
	private String homegearXmlRpcServerHost;
	@Value("${homegear.xmlrpc.server.port}")
	private String homegearXmlRpcServerPort;
	@Value("${homegear.xmlrpc.server.name}")
	private String homegearXmlRpcServerName;
	@Value("${homegear.xmlrpc.server.path}")
	private String homegearXmlRpcServerPath;
	@Value("${homegear.xmlrpc.server.enabled}")
	private boolean serverEnabled;

	@PostConstruct
	public void init() throws Throwable {
		rpcClient = new JsonRpcHttpClient(new URL("http://" + homegearHost + ":" + homegearPort));

		XmlHandler xh = new XmlHandler();
		devices = xh.readFromXml(DeviceConfig.class, "devices.xml");
		createDeviceIdMapping();
		deviceMap = devices.getDeviceMap();

		if (serverEnabled) {
			registerCallbackEventServer(
					"http://" + homegearXmlRpcServerHost + ":" + homegearXmlRpcServerPort + ""
							+ homegearXmlRpcServerPath,
					InetAddress.getLocalHost().getHostName() + ":" + homegearXmlRpcServerName,
					(0x01 + 0x04 + 0x10 + 0x80));
			Log.info("rpc callback event server enabled");
		} else {
			Log.info("rpc callback event server disabled");
		}
	}

	public IRemoteRadiatorThermostat getRemoteRadiatorThermostatBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteRadiatorThermostat smartDevice = (IRemoteRadiatorThermostat) Class.forName(device.getAdapterClassName())
				.newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	public IRemoteWallThermostat getRemoteWallThermostatBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteWallThermostat smartDevice = (IRemoteWallThermostat) Class.forName(device.getAdapterClassName())
				.newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	public IRemoteOutdoorWeatherSensor getRemoteOutdoorWeatherSensorBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteOutdoorWeatherSensor smartDevice = (IRemoteOutdoorWeatherSensor) Class
				.forName(device.getAdapterClassName()).newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	public IRemoteValveDrive getRemoteValveDriveBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteValveDrive smartDevice = (IRemoteValveDrive) Class.forName(device.getAdapterClassName()).newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	public IRemoteMeteringSwitch getRemoteMeteringSwitchBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteMeteringSwitch smartDevice = (IRemoteMeteringSwitch) Class.forName(device.getAdapterClassName())
				.newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	public IRemoteSwitch getRemoteSwitchBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteSwitch smartDevice = (IRemoteSwitch) Class.forName(device.getAdapterClassName()).newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	public IRemoteSmokeDetector getRemoteRemoteSmokeDetectorBySerialNo(String serialNo) throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		IRemoteSmokeDetector smartDevice = (IRemoteSmokeDetector) Class.forName(device.getAdapterClassName())
				.newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;
	}

	@SuppressWarnings("unchecked")
	public <D extends ISmartDevice> List<D> getSmartDeviceList(Class<? super D> deviceClass) throws Throwable {
		List<D> deviceList = new ArrayList<>();
		List<DeviceId> deviceIdList = getDeviceIdList(deviceClass);
		for (DeviceId deviceId : deviceIdList) {
			Device device = getDeviceBySerialNo(deviceId.getAddress());
			D smartDevice = (D) Class.forName(device.getAdapterClassName()).newInstance();
			smartDevice.setSerialNo(device.getSerialNo());
			smartDevice.setHomegearDeviceFactory(this);
			deviceList.add(smartDevice);
		}
		return deviceList;
	}

	@SuppressWarnings("unchecked")
	public <D extends ISmartDevice> D getSmartDeviceBySerialNo(String serialNo, Class<? super D> deviceClass)
			throws Throwable {
		Device device = getDeviceBySerialNo(serialNo);
		D smartDevice = (D) Class.forName(device.getAdapterClassName()).newInstance();
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceFactory(this);
		return smartDevice;

	}

	public String getControlModeText(Integer mode) {
		String controlModeName = "unkown";
		switch (mode) {
		case 0:
			controlModeName = "automatic";
			break;
		case 1:
			controlModeName = "manual";
			break;
		}
		return controlModeName;
	}

	public Object executeMethod(String methodName, Object parameter[]) throws Throwable {
		return rpcClient.invoke(methodName, parameter, Object.class);
	}

	public void registerCallbackEventServer(String url, String interfaceId, Integer flags) throws Throwable {
		executeMethod("init", new Object[] { url, interfaceId, flags });
	}

	public void addLink(Integer senderID, Integer senderChannel, Integer receiverID, Integer receiverChannel,
			String name, String description) throws Throwable {
		executeMethod("addLink",
				new Object[] { senderID, senderChannel, receiverID, receiverChannel, name, description });
	}

	public Object removeLink(Integer senderID, Integer senderChannel, Integer receiverID, Integer receiverChannel)
			throws Throwable {
		return executeMethod("removeLink", new Object[] { senderID, senderChannel, receiverID, receiverChannel });
	}

	public Object removeLink(String senderAddress, String receiverAddress) throws Throwable {
		return executeMethod("removeLink", new Object[] { senderAddress, receiverAddress });
	}

	public Object getAllValues() throws Throwable {
		return executeMethod("getAllValues", new Object[] { new Boolean(true) });
	}

	public DeviceId getDeviceIdBySerialNo(String serialNo) {
		return deviceSerialMap.get(serialNo);
	}

	public <D extends ISmartDevice> List<DeviceId> getDeviceIdList(Class<? super D> deviceClass) throws Throwable {

		List<DeviceId> deviceIdList = new ArrayList<>();
		for (DeviceId id : deviceSerialMap.values()) {
			Device device = deviceMap.get(id.getType());
			if (device != null) {
				Class<?> cls = Class.forName(device.getAdapterClassName());
				if (deviceClass.isAssignableFrom(cls) && deviceIdList.indexOf(id) == -1) {
					deviceIdList.add(id);
				}
			}
		}
		return deviceIdList;
	}

	public void getDeviceInfo() throws Throwable {
		Object o = executeMethod("listDevices",
				new Object[] { new Boolean(false), new Object[] { "VERSION", "TYPE", "ADDRESS" } });
	}

	public Device getDeviceBySerialNo(String serialNo) throws Throwable {

		DeviceId deviceId = getDeviceIdBySerialNo(serialNo);
		return getDevice(deviceId);
	}

	public Device getDevice(DeviceId deviceId) throws Exception {
		Device device = deviceMap.get(deviceId.getType());
		device.setPeerId(deviceId.getId());
		device.setSerialNo(deviceId.getAddress());
		return device;
	}

	@SuppressWarnings("unchecked")
	private void createDeviceIdMapping() throws Throwable {
		deviceSerialMap = new HashMap<>();
		ArrayList<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>) executeMethod(
				"listDevices",
				new Object[] { new Boolean(false), new Object[] { "ID", "ADDRESS", "TYPE", "FAMILYID", "NAME" } });

		for (LinkedHashMap<String, Object> map : list) {
			DeviceId deviceId = new DeviceId();

			for (Object key : map.keySet()) {
				Object value = map.get(key);
				if (key.equals("ID")) {
					deviceId.setId((Integer) value);
				} else if (key.equals("TYPE")) {
					deviceId.setType((String) value);
				} else if (key.equals("ADDRESS")) {
					deviceId.setAddress((String) value);
				} else if (key.equals("FAMILYID")) {
					deviceId.setFamilyId((Integer) value);
				} else if (key.equals("NAME")) {
					deviceId.setLocation((String) value);
				}
			}
			deviceSerialMap.put(deviceId.getAddress(), deviceId);
		}
	}
}