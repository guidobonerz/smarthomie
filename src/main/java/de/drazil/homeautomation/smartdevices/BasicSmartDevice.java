package de.drazil.homeautomation.smartdevices;

import java.util.Map;

import de.drazil.homeautomation.bean.Device;
import de.drazil.homeautomation.bean.DeviceField;
import de.drazil.homeautomation.bean.Type;
import de.drazil.homeautomation.service.HomegearDeviceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicSmartDevice implements IHmAttributes {
	private String serialNo;

	private HomegearDeviceService factory;

	public void setHomegearDeviceFactory(final HomegearDeviceService factory) {
		this.factory = factory;
	}

	public void setSerialNo(final String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public String getLocation() throws Throwable {
		return factory.getDeviceIdBySerialNo(getSerialNo()).getLocation();
	}

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getParamset(final String name, final String typeName) throws Throwable {
		final Device device = factory.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get(typeName);
		final DeviceField field = type.getDeviceFieldMap().get(name);
		return (ValueType) factory.executeMethod("getParamset",
				new Object[] { device.getPeerId(), field.getChannel(), typeName });
	}

	protected void putParamset(final String name, final String typeName, final Map<String, Object> parameterMap)
			throws Throwable {
		final Device device = factory.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get(typeName);
		final DeviceField field = type.getDeviceFieldMap().get(name);
		factory.executeMethod("putParamset",
				new Object[] { device.getPeerId(), field.getChannel(), typeName, parameterMap });
	}

	protected <ValueType> ValueType getValue(final String valueName) throws Throwable {
		return getValue(valueName, null);
	}

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getValue(final String valueName, final Integer channel) throws Throwable {
		final Device device = factory.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get("value");
		final DeviceField field = type.getDeviceFieldMap().get(valueName);

		final Object o = factory.executeMethod("getValue",
				new Object[] { device.getPeerId(), channel == null ? field.getChannel() : channel, valueName });
		ValueType v = null;
		try {
			v = (ValueType) o;
		} catch (final Exception e) {
			log.error("{} wrong type casting  ", valueName, e);
		}
		return v;
	}

	protected <ValueType> void setValue(final String valueName, final ValueType value) throws Throwable {
		setValue(valueName, value, null);
	}

	protected <ValueType> void setValue(final String valueName, final ValueType value, final Integer channel)
			throws Throwable {
		final Device device = factory.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get("value");
		final DeviceField field = type.getDeviceFieldMap().get(valueName);
		factory.executeMethod("setValue",
				new Object[] { device.getPeerId(), channel == null ? field.getChannel() : channel, valueName, value });
	}

}
