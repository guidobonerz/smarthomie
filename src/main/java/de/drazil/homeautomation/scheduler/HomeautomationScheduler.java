package de.drazil.homeautomation.scheduler;

import java.text.MessageFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.drazil.homeautomation.service.HomegearService;
import lombok.extern.slf4j.Slf4j;

@Component("schedulerBean")
@Slf4j
public class HomeautomationScheduler {

	@Autowired
	HomegearService service;

	//@Value("${boiler.valveState1}")
	private String valveState1;
	//@Value("${boiler.valveState2}")
	private String valveState2;
	//@Value("${boiler.control}")
	private String boilerControl;

	@Scheduled(cron = "${boiler.heating1.on}")
	public void setBoilerHeating1On() {
		final String message = MessageFormat.format("Boiler Heating1 - On at {0,time}", new Date());
		log.info(message);
		setBoilerHeatingOn(true);
	}

	// @Scheduled(cron = "${boiler.heating2.on}")
	public void setBoilerHeating2On() {
		final String message = MessageFormat.format("Boiler Heating2 - On at {0,time}", new Date());
		log.info(message);
		setBoilerHeatingOn(true);
	}

	private void setBoilerHeatingOn(final boolean state) {
		try {
			if (boilerControl.equals("on")) {
				service.setBoilerState(1, state);
				// factory.getRemoteValveDriveBySerialNo("HEQ0134004").setValveState(Integer.valueOf(valveState));
			}
		} catch (final Throwable e) {
			log.error("error switchting boiler on");
		}
	}

	// @Scheduled(cron = "${boiler.heating1.off}")
	// @Scheduled(cron = "${boiler.heating2.off}")
	public void setBoilerHeatingOff() {
		final String message = MessageFormat.format("Boiler Heating - Off at {0,time}", new Date());
		log.info(message);
		try {
			if (boilerControl.equals("on")) {
				service.setBoilerState(1, false);
				// factory.getRemoteValveDriveBySerialNo("HEQ0134004").setValveState(0);
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "${floorlamp.on}")
	public void setFloorLampOn() throws Throwable {
		final String message = MessageFormat.format("FloorLamp - On at {0,time}", new Date());
		log.info(message);
		service.setLight("corridor", true);
	}

	// @Scheduled(cron = "${floorlamp.off}")
	public void setFloorLampOff() throws Throwable {
		log.info("FloorLamp - Off");
		service.setLight("corridor", false);
	}

	// @Scheduled(cron = "${livingroomlamp.on}")
	public void setLivingroomLampOn() throws Throwable {
		final String message = MessageFormat.format("LivingroomLamp - On at {0,time}", new Date());
		log.info(message);
		service.setLight("livingroom", true);
	}

	// @Scheduled(cron = "${livingroomlamp.off}")
	public void setLivingroomLampOff() throws Throwable {
		final String message = MessageFormat.format("LivingroomLamp - Off at {0,time}", new Date());
		log.info(message);
		service.setLight("livingroom", false);
	}
}
