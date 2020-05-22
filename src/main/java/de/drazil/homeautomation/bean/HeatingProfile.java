package de.drazil.homeautomation.bean;

import java.util.List;

import de.drazil.homeautomation.smartdevices.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatingProfile {

	private Day days[];
	private List<HeatingPhase> heatingPhases;

	public HeatingProfile(final List<HeatingPhase> heatingPhases) {
		this(Day.ALL_DAYS, heatingPhases);
	}

	public HeatingProfile(final Day day, final List<HeatingPhase> heatingPhases) {
		this.heatingPhases = heatingPhases;
		days = new Day[] { day };
	}

}
