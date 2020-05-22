package de.drazil.homeautomation.bean;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.drazil.homeautomation.xml.DeviceFieldListAdapter;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Type {
	@XmlAttribute
	private String id;

	@XmlElement(name = "fields")
	@XmlJavaTypeAdapter(DeviceFieldListAdapter.class)
	private Map<String, DeviceField> deviceFieldMap;

	public Type() {
		deviceFieldMap = new LinkedHashMap<>();
	}

	public void add(final DeviceField deviceField) {
		deviceFieldMap.put(deviceField.getId(), deviceField);
	}
}
