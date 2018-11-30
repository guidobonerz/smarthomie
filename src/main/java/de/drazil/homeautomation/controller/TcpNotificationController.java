package de.drazil.homeautomation.controller;

import org.springframework.beans.factory.annotation.Autowired;

import de.drazil.homeautomation.service.HomegearEventServiceImpl;
import javagrinko.spring.tcp.Connection;
import javagrinko.spring.tcp.TcpController;

@TcpController
public class TcpNotificationController {
	@Autowired
	HomegearEventServiceImpl eventService;

	public void receiveData(Connection connection, byte[] data) {
		String s = new String(data);
		eventService.event("Camera:" + s, -1, -1, "MotionDetection", true);
		
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
