package de.drazil;

import java.text.MessageFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		System.out.println(MessageFormat.format("{0,time}", new Date()));

	}

}
