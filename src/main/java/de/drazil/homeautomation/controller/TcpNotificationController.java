package de.drazil.homeautomation.controller;

import org.springframework.beans.factory.annotation.Autowired;

import de.drazil.homeautomation.service.HomegearEventServiceImpl;
import javagrinko.spring.tcp.Connection;
import javagrinko.spring.tcp.TcpController;
import lombok.extern.slf4j.Slf4j;

@TcpController
@Slf4j
public class TcpNotificationController {
	@Autowired
	HomegearEventServiceImpl eventService;

	public void receiveData(final Connection connection, final byte[] data) {
		final String s = new String(data);
		eventService.event("Camera:" + s, -1, -1, "MotionDetection", true);

	}

	public void connect(final Connection connection) {
		log.debug("New connection {}", connection.getAddress().getCanonicalHostName());
	}

	public void disconnect(final Connection connection) {
		log.debug("Disconnect {}", connection.getAddress().getCanonicalHostName());
	}
}
