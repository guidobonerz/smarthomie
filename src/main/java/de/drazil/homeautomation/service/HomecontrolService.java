package de.drazil.homeautomation.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomecontrolService {

	@Autowired
	HomegearService homegearService;

	@Autowired
	MessageService messageService;

	@Value("${boiler.workdayTemp}")
	private Double workdayTemp;

	@Value("${boiler.weekendTemp}")
	private Double weekendTemp;

	public void control(String interfaceId, int peerId, int channel, String parameterName, Object value) {
		log.debug("InterfaceId:" + interfaceId + "   PeerId:" + peerId + "   Channel:" + channel + "   ParameterName:"
				+ parameterName);

		if (peerId == 46 && channel == 1 && parameterName.equals("TEMPERATURE")) {
			Number n = ((Number) value);
			log.debug("current boiler temperature is " + n.doubleValue());

			DayOfWeek dow = LocalDate.now().getDayOfWeek();
			double boilerTemp = (dow.compareTo(DayOfWeek.SATURDAY) == 0) ? weekendTemp : workdayTemp;
			if (n.doubleValue() > boilerTemp) {
				try {
					if (homegearService.getBoilerState(1)) {
						homegearService.setBoilerState(1, false);
						log.info("boiler reached max temperature -> switch it off");
					}
				} catch (Throwable e) {

					e.printStackTrace();
				}
			}
		}
	}
}
