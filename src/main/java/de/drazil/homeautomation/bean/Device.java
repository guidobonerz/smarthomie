package de.drazil.homeautomation.bean;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.drazil.homeautomation.xml.TypeListAdapter;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Device {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String vendor;
	@XmlAttribute
	private String description;
	@XmlAttribute
	private String adapterClassName;
	public Integer peerId;
	public String serialNo;
	@XmlElement(name = "types")
	@XmlJavaTypeAdapter(TypeListAdapter.class)
	private Map<String, Type> typeMap;

	public Device() {
		typeMap = new LinkedHashMap<String, Type>();
	}

	public void add(Type type) {
		typeMap.put(type.getId(), type);
	}
}
