package de.drazil.homegear.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.drazil.homegear.Day;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatingProfile {

	private Day days[];
	private List<HeatingPhase> heatingPhases;

	public HeatingProfile(List<HeatingPhase> heatingPhases) {
		this(Day.ALL_DAYS, heatingPhases);
	}

	public HeatingProfile(Day day, List<HeatingPhase> heatingPhases) {
		this.heatingPhases = heatingPhases;
		days = new Day[] { day };
	}

}
