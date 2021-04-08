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

	private Double temporaryTemperature;

	private boolean temporaryMode;

	public void setTemporaryTemperture(Double temperature) {
		temporaryMode = true;
		temporaryTemperature = temperature;
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

				final DayOfWeek dow = LocalDate.now().getDayOfWeek();
				double boilerTemp = (dow.compareTo(DayOfWeek.SATURDAY) == 0) ? weekendTemp : workdayTemp;
				if (temporaryMode) {
					boilerTemp = temporaryTemperature;
				}
				if (n.doubleValue() > boilerTemp) {
					temporaryMode = false;
					homegearService.setBoilerState(1, false);
					log.info("boiler reached max temperature -> switch it off");
				}
			}
		} catch (final Throwable e) {
			log.error("error setting boiler state", e);
		}
	}
}
