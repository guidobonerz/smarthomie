package de.drazil.homeautomation.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceField
{
	@XmlAttribute
	private String id;
	@XmlAttribute
	private Integer channel;
	@XmlAttribute
	private Boolean readOnly;
}
