package de.drazil.homeautomation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceId
{
	private Integer familyId;
	private Integer id;
	private String type;
	private String address;
	private String location;
}
