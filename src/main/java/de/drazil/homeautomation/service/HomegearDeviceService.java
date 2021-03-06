package de.drazil.homeautomation.service;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import de.drazil.homeautomation.smartdevicesimpl.homematic.HomematicTemperatureDifferenceSensor;
import de.drazil.homeautomation.xml.XmlHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomegearDeviceService {

	private JsonRpcHttpClient rpcClient = null;
	private DeviceConfig devices = null;
	private Map<String, DeviceId> deviceSerialMap;
	private Map<String, DeviceId> deviceLocationMap;
	private Map<String, DeviceId> deviceIdMap;
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

		final XmlHandler xh = new XmlHandler();
		devices = xh.readFromXml(DeviceConfig.class, "devices.xml");
		createDeviceMapping();
		deviceMap = devices.getDeviceMap();

		if (serverEnabled) {
			registerCallbackEventServer(
					"http://" + homegearXmlRpcServerHost + ":" + homegearXmlRpcServerPort + ""
							+ homegearXmlRpcServerPath,
					InetAddress.getLocalHost().getHostName() + ":" + homegearXmlRpcServerName,
					(0x01 + 0x04 + 0x10 + 0x20 + 0x80));
			log.info("rpc callback event server enabled");
		} else {
			log.info("rpc callback event server disabled");
		}
	}

	public IRemoteRadiatorThermostat getRemoteRadiatorThermostatBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteRadiatorThermostat smartDevice = (IRemoteRadiatorThermostat) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public IRemoteWallThermostat getRemoteWallThermostatBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteWallThermostat smartDevice = (IRemoteWallThermostat) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public IRemoteOutdoorWeatherSensor getRemoteOutdoorWeatherSensorBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteOutdoorWeatherSensor smartDevice = (IRemoteOutdoorWeatherSensor) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public IRemoteValveDrive getRemoteValveDriveBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteValveDrive smartDevice = (IRemoteValveDrive) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public IRemoteMeteringSwitch getRemoteMeteringSwitchBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteMeteringSwitch smartDevice = (IRemoteMeteringSwitch) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public HomematicTemperatureDifferenceSensor getTemperatureDifferenceSensorBySerialNo(final String serialNo)
			throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final HomematicTemperatureDifferenceSensor smartDevice = (HomematicTemperatureDifferenceSensor) getDeviceInstance(
				device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public IRemoteSwitch getRemoteSwitchBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteSwitch smartDevice = (IRemoteSwitch) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public IRemoteSmokeDetector getRemoteRemoteSmokeDetectorBySerialNo(final String serialNo) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final IRemoteSmokeDetector smartDevice = (IRemoteSmokeDetector) getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;
	}

	public <D extends ISmartDevice> List<D> getSmartDeviceList(final Class<? super D> deviceClass) throws Throwable {
		final List<D> deviceList = new ArrayList<>();
		final List<DeviceId> deviceIdList = getDeviceIdList(deviceClass);
		for (final DeviceId deviceId : deviceIdList) {
			final Device device = getDeviceBySerialNo(deviceId.getAddress());
			final D smartDevice = getDeviceInstance(device);
			smartDevice.setSerialNo(device.getSerialNo());
			smartDevice.setHomegearDeviceService(this);
			deviceList.add(smartDevice);
		}
		return deviceList;
	}

	public <D extends ISmartDevice> D getSmartDeviceBySerialNo(final String serialNo,
			final Class<? super D> deviceClass) throws Throwable {
		final Device device = getDeviceBySerialNo(serialNo);
		final D smartDevice = getDeviceInstance(device);
		smartDevice.setSerialNo(device.getSerialNo());
		smartDevice.setHomegearDeviceService(this);
		return smartDevice;

	}

	@SuppressWarnings("unchecked")
	private <D extends ISmartDevice> D getDeviceInstance(Device device) throws Throwable {
		return (D) Class.forName(device.getAdapterClassName()).getDeclaredConstructor().newInstance();
	}

	public String getControlModeText(final Integer mode) {
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

	public Object executeMethod(final String methodName, final Object parameter[]) throws Throwable {
		return rpcClient.invoke(methodName, parameter, Object.class);
	}

	public void registerCallbackEventServer(final String url, final String interfaceId, final Integer flags)
			throws Throwable {
		log.info(url);
		executeMethod("init", new Object[] { url, interfaceId, flags });
	}

	public void addLink(final Integer senderID, final Integer senderChannel, final Integer receiverID,
			final Integer receiverChannel, final String name, final String description) throws Throwable {
		executeMethod("addLink",
				new Object[] { senderID, senderChannel, receiverID, receiverChannel, name, description });
	}

	public Object removeLink(final Integer senderID, final Integer senderChannel, final Integer receiverID,
			final Integer receiverChannel) throws Throwable {
		return executeMethod("removeLink", new Object[] { senderID, senderChannel, receiverID, receiverChannel });
	}

	public Object removeLink(final String senderAddress, final String receiverAddress) throws Throwable {
		return executeMethod("removeLink", new Object[] { senderAddress, receiverAddress });
	}

	public Object getAllValues() throws Throwable {
		return executeMethod("getAllValues", new Object[] { Boolean.TRUE });
	}

	public DeviceId getDeviceIdBySerialNo(final String serialNo) {
		return deviceSerialMap.get(serialNo);
	}

	public <D extends ISmartDevice> List<DeviceId> getDeviceIdList(final Class<? super D> deviceClass)
			throws Throwable {

		final List<DeviceId> deviceIdList = new ArrayList<>();
		for (final DeviceId id : deviceSerialMap.values()) {
			final Device device = deviceMap.get(id.getType());
			if (device != null) {
				final Class<?> cls = Class.forName(device.getAdapterClassName());
				if (deviceClass.isAssignableFrom(cls) && deviceIdList.indexOf(id) == -1) {
					deviceIdList.add(id);
				}
			}
		}
		return deviceIdList;
	}

	public void getDeviceInfo() throws Throwable {
		executeMethod("listDevices", new Object[] { Boolean.FALSE, new Object[] { "VERSION", "TYPE", "ADDRESS" } });
	}

	public Device getDeviceBySerialNo(final String serialNo) throws Throwable {
		final DeviceId deviceId = getDeviceIdBySerialNo(serialNo);
		return getDevice(deviceId);
	}

	public Device getDevice(final DeviceId deviceId) throws Exception {
		final Device device = deviceMap.get(deviceId.getType());
		device.setPeerId(deviceId.getId());
		device.setSerialNo(deviceId.getAddress());
		return device;
	}

	@SuppressWarnings("unchecked")
	private void createDeviceMapping() throws Throwable {
		deviceSerialMap = new HashMap<>();
		deviceLocationMap = new HashMap<>();
		deviceIdMap = new HashMap<>();
		final ArrayList<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>) executeMethod(
				"listDevices",
				new Object[] { Boolean.FALSE, new Object[] { "ID", "ADDRESS", "TYPE", "FAMILYID", "NAME" } });

		for (final LinkedHashMap<String, Object> map : list) {
			final DeviceId deviceId = new DeviceId();

			for (final Object key : map.keySet()) {
				final Object value = map.get(key);
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
			deviceLocationMap.put(deviceId.getLocation(), deviceId);
			deviceIdMap.put(deviceId.getId().toString(), deviceId);
		}
	}

	public DeviceId getDeviceId(final String id) {
		return deviceIdMap.get(id);
	}
}