package de.drazil.homeautomation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import de.drazil.homeautomation.dto.SmartDeviceEvent;

@Service
@AutoJsonRpcServiceImpl
public class HomegearEventServiceImpl implements IHomegearEventService {
	private static final Logger Log = Logger.getLogger("jsonrpchandler");

	private List<SmartDeviceEvent> smartDeviceEventList = null;

	@Autowired
	HomecontrolService homecontrolService;

	@PostConstruct
	public void init() {
		smartDeviceEventList = new ArrayList<>();
	}

	private static List<String> rpcMethodList = new ArrayList<String>();
	static {
		rpcMethodList.add("system.listMethods");
		rpcMethodList.add("event");
		rpcMethodList.add("error");
		// rpcMethodList.add("listDevices");
		// rpcMethodList.add("newDevices");
	}

	public List<SmartDeviceEvent> getSmartDeviceEventList() {
		List<SmartDeviceEvent> eventList = new ArrayList<>(smartDeviceEventList);
		for (SmartDeviceEvent event : eventList) {
			smartDeviceEventList.remove(event);
		}
		return eventList;
	}

	@JsonRpcMethod("event")
	public void event(String interfaceId, int peerId, int channel, String parameterName, Object value) {
		homecontrolService.control(interfaceId, peerId, channel, parameterName, value);
		if (smartDeviceEventList.size() < 100) {
			smartDeviceEventList.add(new SmartDeviceEvent(interfaceId, peerId, channel, parameterName, value));
		}
	}

	@JsonRpcMethod("error")
	public void error(String interfaceId, Integer level, String message) {
		Log.info("ERROR > interfaceId: " + interfaceId + " level: " + level + " message " + message);
	}

	@JsonRpcMethod("listMethods")
	public List<String> listMethods() {
		// Log.info("listMethods");
		return rpcMethodList;
	}
}
