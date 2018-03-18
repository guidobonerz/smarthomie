package de.drazil.homegear.bean;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import de.drazil.xml.DeviceFieldListAdapter;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Type {
	@XmlAttribute
	private String id;

	@XmlElement(name = "fields")
	@XmlJavaTypeAdapter(DeviceFieldListAdapter.class)
	private Map<String, DeviceField> deviceFieldMap;

	public Type() {
		deviceFieldMap = new LinkedHashMap<String, DeviceField>();
	}

	public void add(DeviceField deviceField) {
		deviceFieldMap.put(deviceField.getId(), deviceField);
	}
}
