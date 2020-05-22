package de.drazil.homeautomation.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.drazil.homeautomation.bean.DeviceId;
import de.drazil.homeautomation.dto.SmartDeviceEvent;
import lombok.extern.slf4j.Slf4j;

@Service
@AutoJsonRpcServiceImpl
@Slf4j
public class HomegearEventServiceImpl implements IHomegearEventService {

	@Autowired
	HomecontrolService homecontrolService;
	@Autowired
	MessageService messageService;
	@Autowired
	HomegearDeviceService homegearDeviceService;

	private static List<String> rpcMethodList = new ArrayList<>();
	static {
		rpcMethodList.add("system.listMethods");
		rpcMethodList.add("event");
		rpcMethodList.add("error");
		// rpcMethodList.add("listDevices");
		// rpcMethodList.add("newDevices");
	}

	@JsonRpcMethod("event")
	public void event(final String interfaceId, final int peerId, final int channel, final String parameterName,
			final Object value) {
		homecontrolService.control(interfaceId, peerId, channel, parameterName, value);
		if (messageService.getMessageCount() < 100) {
			final DeviceId id = homegearDeviceService.getDeviceId(Integer.toString(peerId));
			messageService.addMessage(new Message("EVENT", new SmartDeviceEvent(id.getLocation(), id.getAddress(),
					interfaceId, peerId, channel, parameterName, value)));
		}
	}

	@JsonRpcMethod("error")
	public void error(final String interfaceId, final Integer level, final String message) {
		log.info("ERROR > interfaceId: {} level: {} message: {}", interfaceId, level, message);
	}

	@JsonRpcMethod("listMethods")
	public List<String> listMethods() {
		// Log.info("listMethods");
		return rpcMethodList;
	}
}
