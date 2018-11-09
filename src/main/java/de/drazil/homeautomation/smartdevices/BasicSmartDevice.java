package de.drazil.homeautomation.smartdevices;

import java.util.Map;

import de.drazil.homeautomation.bean.Device;
import de.drazil.homeautomation.bean.DeviceField;
import de.drazil.homeautomation.bean.Type;
import de.drazil.homeautomation.service.HomegearDeviceService;

public class BasicSmartDevice {
	private String serialNo;

	private HomegearDeviceService factory;

	public void setHomegearDeviceFactory(HomegearDeviceService factory) {
		this.factory = factory;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public String getLocation() throws Throwable {
		return factory.getDeviceIdBySerialNo(getSerialNo()).getLocation();
	}

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getParamset(String name, String typeName) throws Throwable {
		Device device = factory.getDeviceBySerialNo(getSerialNo());
		Type type = device.getTypeMap().get(typeName);
		DeviceField field = type.getDeviceFieldMap().get(name);
		return (ValueType) factory.executeMethod("getParamset",
				new Object[] { device.getPeerId(), field.getChannel(), typeName });
	}

	protected void putParamset(String name, String typeName, Map<String, Object> parameterMap) throws Throwable {
		Device device = factory.getDeviceBySerialNo(getSerialNo());
		Type type = device.getTypeMap().get(typeName);
		DeviceField field = type.getDeviceFieldMap().get(name);
		factory.executeMethod("putParamset",
				new Object[] { device.getPeerId(), field.getChannel(), typeName, parameterMap });
	}

	protected <ValueType> ValueType getValue(String valueName) throws Throwable {
		return getValue(valueName, null);
	}

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getValue(String valueName, Integer channel) throws Throwable {
		Device device = factory.getDeviceBySerialNo(getSerialNo());
		Type type = device.getTypeMap().get("value");
		DeviceField field = type.getDeviceFieldMap().get(valueName);

		Object o = factory.executeMethod("getValue",
				new Object[] { device.getPeerId(), channel == null ? field.getChannel() : channel, valueName });
		ValueType v = null;
		try {
			v = (ValueType) o;
		} catch (Exception e) {
			System.out.println(valueName + " wrong type casting  " + e.toString());
			e.printStackTrace();
		}
		return v;
	}

	protected <ValueType> void setValue(String valueName, ValueType value) throws Throwable {
		Device device = factory.getDeviceBySerialNo(getSerialNo());
		Type type = device.getTypeMap().get("value");
		DeviceField field = type.getDeviceFieldMap().get(valueName);
		factory.executeMethod("setValue", new Object[] { device.getPeerId(), field.getChannel(), valueName, value });
	}

}
