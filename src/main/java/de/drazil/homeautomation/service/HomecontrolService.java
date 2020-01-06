package de.drazil.homeautomation.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HomecontrolService {

	private static final Logger log = Logger.getLogger("HomecontrolService");
	@Autowired
	HomegearService homegearService;

	@Autowired
	MessageService messageService;

	@Value("${boiler.workdayTemp}")
	private Double workdayTemp;

	@Value("${boiler.weekendTemp}")
	private Double weekendTemp;

	public void control(String interfaceId, int peerId, int channel, String parameterName, Object value) {
		log.finest("InterfaceId:" + interfaceId + "   PeerId:" + peerId + "   Channel:" + channel + "   ParameterName:"
				+ parameterName);

		if (peerId == 46 && channel == 1 && parameterName.equals("TEMPERATURE")) {
			Number n = ((Number) value);
			log.fine("current boiler temperature is " + n.doubleValue());

			DayOfWeek dow = LocalDate.now().getDayOfWeek();
			double boilerTemp = (dow.compareTo(DayOfWeek.SATURDAY) == 0) ? weekendTemp : workdayTemp;
			if (n.doubleValue() > boilerTemp) {
				try {
					if (homegearService.getBoilerState(1)) {
						homegearService.setBoilerState(1, false);
						// messageService.addMessage(new Message("EVENT", payload));
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
