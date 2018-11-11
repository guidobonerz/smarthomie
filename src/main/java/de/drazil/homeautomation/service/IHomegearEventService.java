package de.drazil.homeautomation.service;

import java.util.List;

import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/jsonrpc")
public interface IHomegearEventService {

	public void event(String interfaceId, int peerId, int channel, String parameterName, Object value);

	public void error(String interfaceId, Integer level, String message);

	public List<String> listMethods();
}
