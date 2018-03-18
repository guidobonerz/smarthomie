package de.drazil.homegear.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SwitchState {
	private String serialNo;
	private boolean state;
}
