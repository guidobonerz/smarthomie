package de.drazil.homegear.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

@Service
@AutoJsonRpcServiceImpl
public class HomegearJsonRpcImpl implements IHomegearJsonRpc {
	private static final Logger Log = Logger.getLogger("jsonrpchandler");

	private static List<String> rpcMethodList = new ArrayList<String>();
	static {
		rpcMethodList.add("system.listMethods");
		rpcMethodList.add("event");
		rpcMethodList.add("error");
		// rpcMethodList.add("listDevices");
		// rpcMethodList.add("newDevices");
	}

	@JsonRpcMethod("event")
	public void event(String interfaceId, int peerId, int channel, String parameterName, Object value) {
		// Log.info("EVENT > interfaceId: " + interfaceId + " peerId: " + peerId + "
		// channel: " + channel + " parameterName: " + parameterName + " value: " +
		// value);
	}

	@JsonRpcMethod("error")
	public void error(String interfaceId, Integer level, String message) {
		// Log.info("ERROR > interfaceId: " + interfaceId + " level: " + level + "
		// message " + message);
	}

	@JsonRpcMethod("listMethods")
	public List<String> listMethods() {
		// Log.info("listMethods");
		return rpcMethodList;
	}
}
