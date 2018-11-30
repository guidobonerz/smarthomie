package de.drazil.homeautomation.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomecontrolService {

	private static final Logger Log = Logger.getLogger("HomecontrolService");
	@Autowired
	HomegearService homegearService;

	public void control(String interfaceId, int peerId, int channel, String parameterName, Object value) {
		// System.out.println(peerId + ":" + channel + ":" + parameterName);
		if (peerId == 46 && channel == 2 && parameterName.equals("TEMPERATURE")) {
			Number n = ((Number) value);
			Log.info("current boiler temperature is " + n.doubleValue());

			if (n.doubleValue() > 65) {
				try {
					homegearService.setBoiler(false);
					Log.info("boiler reached max temperature -> switch it off");
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
