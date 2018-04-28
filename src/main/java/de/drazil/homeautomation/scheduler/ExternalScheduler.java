package de.drazil.homeautomation.scheduler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExternalScheduler {
	private static final Logger log = LoggerFactory.getLogger(ExternalScheduler.class);

	// @Scheduled(cron = "*/10 * * * * *")
	public void getWeatherData() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(
				"http://api.openweathermap.org/data/2.5/weather?q=Dinslaken,de&units=metric&APPID=cce438ee0049647cc63adb6598fd65c4");

		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			// Read the contents of an entity and return it as a String.
			String content = EntityUtils.toString(entity);
			log.info(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "*/10 * * * * *")
	public void getSunData() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("https://api.sunrise-sunset.org/json?lat=51.5674264&lng=6.747534");

		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			// Read the contents of an entity and return it as a String.
			String content = EntityUtils.toString(entity);
			log.info(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "*/10 * * * * *")
	public void getWasteCalender() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(
				"https://www.dinslaken.de/www/web_io.nsf/index.xsp?rule=neu&path=%2Fsys%2Fdienstleistungslayout-abfallservice-ausgabe-2%2F&Bezirk=Rilkeweg&AS_Rest=14+t%C3%A4gige+Leerung");

		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			// Read the contents of an entity and return it as a String.
			String content = EntityUtils.toString(entity);
			log.info(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "*/10 * * * * *")
	public void getRainViewerImage() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(
				"http://tilecache.rainviewer.com/" + System.currentTimeMillis() + 1000 + "/2-1-2-1-512.png");

		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			// Read the contents of an entity and return it as a String.
			String content = EntityUtils.toString(entity);
			log.info(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String cronConfig() {
		String cronTabExpression = "*/5 * * * * *";
		/*
		 * if (defaultConfigDto != null && !defaultConfigDto.getFieldValue().isEmpty())
		 * { cronTabExpression = "0 0 4 * * ?"; }
		 */

		return cronTabExpression;
	}
	/*
	 * @Override public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
	 * 
	 * taskRegistrar.addTriggerTask(new Runnable() {
	 * 
	 * @Override public void run() { // paymentService.processPayment(); } }, new
	 * Trigger() {
	 * 
	 * @Override public Date nextExecutionTime(TriggerContext triggerContext) {
	 * String cron = cronConfig(); log.info(cron); CronTrigger trigger = new
	 * CronTrigger(cron); Date nextExec = trigger.nextExecutionTime(triggerContext);
	 * return nextExec; } });
	 * 
	 * }
	 */
}
