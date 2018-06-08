package de.drazil;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		
		String epochString = "1523138400";
		LocalTime.parse("7:11:14 PM", DateTimeFormatter.ofPattern("h:m:s a"));
		LocalTime.parse("3:11:14 AM", DateTimeFormatter.ofPattern("h:m:s a"));
		System.out.println(MessageFormat.format("{0,time}", new Date()));

	}

}
