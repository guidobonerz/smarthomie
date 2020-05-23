package de.drazil.homeautomation.smartdevices;

import java.util.Map;

import de.drazil.homeautomation.bean.Device;
import de.drazil.homeautomation.bean.DeviceField;
import de.drazil.homeautomation.bean.Type;
import de.drazil.homeautomation.service.HomegearDeviceService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Data
public class BasicSmartDevice {
	private String serialNo;

	private HomegearDeviceService homegearDeviceService;

	public String getLocation() throws Throwable {
		return homegearDeviceService.getDeviceIdBySerialNo(getSerialNo()).getLocation();
	}

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getParamset(final HmAttributes name, final String typeName) throws Throwable {
		final Device device = homegearDeviceService.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get(typeName);
		final DeviceField field = type.getDeviceFieldMap().get(name.getName());
		return (ValueType) homegearDeviceService.executeMethod("getParamset",
				new Object[] { device.getPeerId(), field.getChannel(), typeName });
	}

	protected void putParamset(final HmAttributes name, final String typeName, final Map<String, Object> parameterMap)
			throws Throwable {
		final Device device = homegearDeviceService.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get(typeName);
		final DeviceField field = type.getDeviceFieldMap().get(name.getName());
		homegearDeviceService.executeMethod("putParamset",
				new Object[] { device.getPeerId(), field.getChannel(), typeName, parameterMap });
	}

	protected <ValueType> ValueType getValue(final HmAttributes valueName) throws Throwable {
		return getValue(valueName, null);
	}

	@SuppressWarnings("unchecked")
	protected <ValueType> ValueType getValue(final HmAttributes valueName, final Integer channel) throws Throwable {
		final Device device = homegearDeviceService.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get("value");
		final DeviceField field = type.getDeviceFieldMap().get(valueName.getName());

		final Object o = homegearDeviceService.executeMethod("getValue",
				new Object[] { device.getPeerId(), channel == null ? field.getChannel() : channel, valueName });
		ValueType v = null;
		try {
			v = (ValueType) o;
		} catch (final Exception e) {
			log.error("{} wrong type casting  ", valueName.getName(), e);
		}
		return v;
	}

	protected <ValueType> void setValue(final HmAttributes valueName, final ValueType value) throws Throwable {
		setValue(valueName, value, null);
	}

	protected <ValueType> void setValue(final HmAttributes valueName, final ValueType value, final Integer channel)
			throws Throwable {
		final Device device = homegearDeviceService.getDeviceBySerialNo(getSerialNo());
		final Type type = device.getTypeMap().get("value");
		final DeviceField field = type.getDeviceFieldMap().get(valueName.getName());
		homegearDeviceService.executeMethod("setValue",
				new Object[] { device.getPeerId(), channel == null ? field.getChannel() : channel, valueName, value });
	}

}
