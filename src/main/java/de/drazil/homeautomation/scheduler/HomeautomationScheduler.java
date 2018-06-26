package de.drazil.homeautomation.scheduler;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.drazil.homeautomation.service.HomegearDeviceService;

@Component("schedulerBean")
public class HomeautomationScheduler {
	private static final Logger Log = Logger.getLogger("ScheduleController");

	@Autowired
	HomegearDeviceService factory;

	@Value("${boiler.valveState1}")
	private String valveState1;
	@Value("${boiler.valveState2}")
	private String valveState2;
	@Value("${boiler.control}")
	private String boilerControl;

	@Scheduled(cron = "${boiler.heating1.on}")
	public void setBoilerHeating1On() {
		String message = MessageFormat.format("Boiler Heating1 - On at {0,time}", new Date());
		Log.info(message);
		setBoilerHeatingOn(valveState1);
	}

	// @Scheduled(cron = "${boiler.heating2.on}")
	public void setBoilerHeating2On() {
		String message = MessageFormat.format("Boiler Heating2 - On at {0,time}", new Date());
		Log.info(message);
		setBoilerHeatingOn(valveState1);
	}

	private void setBoilerHeatingOn(String valveState) {
		try {
			if (boilerControl.equals("on")) {
				factory.getRemoteValveDriveBySerialNo("HEQ0134004").setValveState(Integer.valueOf(valveState));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "${boiler.heating1.off}")
	// @Scheduled(cron = "${boiler.heating2.off}")
	public void setBoilerHeatingOff() {
		String message = MessageFormat.format("Boiler Heating - Off at {0,time}", new Date());
		Log.info(message);
		try {
			if (boilerControl.equals("on")) {
				factory.getRemoteValveDriveBySerialNo("HEQ0134004").setValveState(0);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "${floorlamp.on}")
	public void setFloorLampOn() {
		String message = MessageFormat.format("FloorLamp - On at {0,time}", new Date());
		Log.info(message);
		setLamp1("LEQ0531814", true);
	}

	@Scheduled(cron = "${floorlamp.off}")
	public void setFloorLampOff() {
		Log.info("FloorLamp - Off");
		setLamp1("LEQ0531814", false);
	}

	private void setLamp1(String serialNo, boolean state) {
		try {
			factory.getRemoteMeteringSwitchBySerialNo(serialNo).setState(state);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "${livingroomlamp.on}")
	public void setLivingroomLampOn() {
		String message = MessageFormat.format("LivingroomLamp - On at {0,time}", new Date());
		Log.info(message);

		setLamp2("OEQ0479803", true);
	}

	@Scheduled(cron = "${livingroomlamp.off}")
	public void setLivingroomLampOff() {
		String message = MessageFormat.format("LivingroomLamp - Off at {0,time}", new Date());
		Log.info(message);
		setLamp2("OEQ0479803", false);
	}

	private void setLamp2(String serialNo, boolean state) {
		try {
			factory.getRemoteSwitchBySerialNo(serialNo).setState(state);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
