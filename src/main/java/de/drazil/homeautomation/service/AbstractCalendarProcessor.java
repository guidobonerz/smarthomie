package de.drazil.homeautomation.service;

public abstract class AbstractCalendarProcessor implements ICalendarProcessor {

	public abstract String getSchedule();

	public abstract void run();
}
