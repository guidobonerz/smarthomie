package de.drazil.homeautomation.controller;

import javagrinko.spring.tcp.Connection;
import javagrinko.spring.tcp.TcpController;

@TcpController
public class TcpNotificationController {

	public void receiveData(Connection connection, byte[] data) {
		String s = new String(data);
		System.out.println(s);

	}

	public void connect(Connection connection) {
		// System.out.println("New connection " +
		// connection.getAddress().getCanonicalHostName());
	}

	public void disconnect(Connection connection) {
		// System.out.println("Disconnect " +
		// connection.getAddress().getCanonicalHostName());
	}

}
