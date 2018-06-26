package de.drazil.homeautomation.smartdevices;

public enum Day {
	ALL_DAYS("ALL_DAYS"), WORK_DAYS("WORK_DAYS"), WEEKEND_DAYS("WEEKEND_DAYS"), MONDAY(
			"MONDAY"), TUESDAY("TUESDAY"), WEDNESDAY("WEDNESDAY"), THURSDAY(
			"THURSDAY"), FRIDAY("FRIDAY"), SATURDAY("SATURDAY"), SUNDAY(
			"SUNDAY");

	private final String name;

	Day(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static Day[] getWeekendDays() {
		return new Day[] { SATURDAY, SUNDAY };
	}

	public static Day[] getWorkDays() {
		return new Day[] { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY };
	}

	public static Day[] getAllDays() {
		return new Day[] { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
				SATURDAY, SUNDAY };
	}

}
