package de.drazil.homeautomation.smartdevices;

public enum WeekProgram {
	WEEK_PROGRAM_1("WEEK PROGRAM 1"), WEEK_PROGRAM_2("WEEK PROGRAM 2"), WEEK_PROGRAM_3(
			"WEEK PROGRAM 3");

	private final String name;

	WeekProgram(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
