package de.drazil.homeautomation.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomecontrolService {

	private static final Logger log = Logger.getLogger("HomecontrolService");
	@Autowired
	HomegearService homegearService;

	public void control(String interfaceId, int peerId, int channel, String parameterName, Object value) {
		log.finest("InterfaceId:" + interfaceId + "   PeerId:" + peerId + "   Channel:" + channel + "   ParameterName:"
				+ parameterName);

		if (peerId == 46 && channel == 1 && parameterName.equals("TEMPERATURE")) {
			Number n = ((Number) value);
			log.fine("current boiler temperature is " + n.doubleValue());

			if (n.doubleValue() > 65) {
				try {
					if (homegearService.getBoilerState(1)) {
						homegearService.setBoilerState(1, false);
						log.info("boiler reached max temperature -> switch it off");
					}
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
