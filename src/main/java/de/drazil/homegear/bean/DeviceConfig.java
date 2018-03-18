package de.drazil.homegear.bean;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import de.drazil.xml.DeviceListAdapter;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceConfig
{

	@XmlElement(name = "devices")
	@XmlJavaTypeAdapter(DeviceListAdapter.class)
	private Map<String, Device> deviceMap;

	public DeviceConfig()
	{
		deviceMap = new LinkedHashMap<String, Device>();
	}

	public void add(Device device)
	{
		deviceMap.put(device.getId(), device);
	}
}
