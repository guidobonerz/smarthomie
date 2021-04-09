package de.drazil.homeautomation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomecontrolService {

	@Autowired
	HomegearService homegearService;

	@Autowired
	MessageService messageService;

	private Double temperature;

	public void setTemperture(Double temperature) {
		this.temperature = temperature;
	}

	public void control(final String interfaceId, final int peerId, final int channel, final String parameterName,
			final Object value) {
		log.debug("InterfaceId:" + interfaceId + "   PeerId:" + peerId + "   Channel:" + channel + "   ParameterName:"
				+ parameterName);
		try {
			if (peerId == 46 && channel == 1 && parameterName.equals("TEMPERATURE")
					&& homegearService.getBoilerState(1)) {
				final Number n = ((Number) value);
				log.debug("current boiler temperature is " + n.doubleValue());

				if (n.doubleValue() > temperature) {
					homegearService.setBoilerState(1, false);
					log.info("boiler reached max temperature -> switch it off");
				}
			}
		} catch (final Throwable e) {
			log.error("error setting boiler state", e);
		}
	}
}
