package de.drazil.homegear;

import java.util.Map;

import de.drazil.homeautomation.service.HomegearDeviceService;
import de.drazil.homegear.bean.Device;
import de.drazil.homegear.bean.DeviceField;
import de.drazil.homegear.bean.Type;

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

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getValue(String valueName) throws Throwable {
		Device device = factory.getDeviceBySerialNo(getSerialNo());
		Type type = device.getTypeMap().get("value");
		DeviceField field = type.getDeviceFieldMap().get(valueName);
		return (ValueType) factory.executeMethod("getValue",
				new Object[] { device.getPeerId(), field.getChannel(), valueName });
	}

	protected <ValueType> void setValue(String valueName, ValueType value) throws Throwable {
		Device device = factory.getDeviceBySerialNo(getSerialNo());
		Type type = device.getTypeMap().get("value");
		DeviceField field = type.getDeviceFieldMap().get(valueName);
		factory.executeMethod("setValue", new Object[] { device.getPeerId(), field.getChannel(), valueName, value });
	}

}
