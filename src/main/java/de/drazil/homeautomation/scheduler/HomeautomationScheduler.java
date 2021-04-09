package de.drazil.homeautomation.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.drazil.homeautomation.service.HomegearService;
import lombok.extern.slf4j.Slf4j;

@Component("schedulerBean")
@Slf4j
public class HomeautomationScheduler {

	@Autowired
	HomegearService service;

	// @Value("${boiler.control}")
	private String boilerControl;

	// @Scheduled(cron = "${boiler.heating.on}")
	/*
	 * public void setBoilerHeating1On() { final String message =
	 * MessageFormat.format("Boiler Heating1 - On at {0,time}", new Date());
	 * log.info(message); setBoilerHeatingOn(true); }
	 * 
	 * private void setBoilerHeatingOn(final boolean state) { try { if
	 * (boilerControl.equals("on")) { service.setBoilerState(1, state); } } catch
	 * (final Throwable e) { log.error("error switchting boiler on"); } }
	 * 
	 * public void setBoilerHeatingOff() { final String message =
	 * MessageFormat.format("Boiler Heating - Off at {0,time}", new Date());
	 * log.info(message); try { if (boilerControl.equals("on")) {
	 * service.setBoilerState(1, false); } } catch (final Throwable e) {
	 * e.printStackTrace(); } }
	 */
}
