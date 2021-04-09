package de.drazil.homeautomation.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.drazil.homeautomation.bean.ResponseWrapper;
import de.drazil.homeautomation.dto.Event;
import de.drazil.homeautomation.service.ExternalSchedulerService;
import de.drazil.homeautomation.service.HomecontrolService;
import de.drazil.homeautomation.service.HomegearService;
import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;
import de.drazil.homeautomation.smartdevices.IRemoteWallThermostat;
import de.drazil.homeautomation.smartdevicesimpl.homematic.HomematicRemoteRadiatorThermostat;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomegearController {
	@Autowired
	private HomegearService homegearService;

	@Autowired
	private ExternalSchedulerService service;

	@Autowired
	private HomecontrolService homecontrol;

	@Autowired
	private TelegramBot bot;

	@Value("${app.base-path}")
	private String basePath;

	@Value("${app.snapshot-path}")
	private String snapshotPath;

	@Value("${app.camera.entrance}")
	private String cameraEntranceIp;

	@Value("${app.camera.corridor}")
	private String cameraCorridorIp;

	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}

	@GetMapping("/index")
	public String index() {
		return "redirect:/overview";
	}

	@GetMapping("/overview")
	public String overview() {
		return "overview";
	}

	@GetMapping("/dashboard")
	public String dashboard(final Model model) {
		model.addAttribute("cameraEntranceIp", cameraEntranceIp);
		model.addAttribute("cameraCorridorIp", cameraCorridorIp);
		return "dashboard";
	}

	@GetMapping("/polling")
	public String polling() {
		return "polling";
	}

	@GetMapping("/floorplan")
	public String floorplan() {
		return "floorplan";
	}

	@GetMapping("/snapshot")
	@ResponseBody
	public ResponseWrapper snapshot(@RequestParam(value = "deviceName", required = true) final String deviceName) {
		final ResponseWrapper rw = new ResponseWrapper(false, "snapshot successfully taken");

		final String pattern = "yyyyddMM-HHmmss";
		final DateFormat df = new SimpleDateFormat(pattern);
		final Date today = Calendar.getInstance().getTime();

		basePath = basePath.replace("{userhome}", System.getProperty("user.home"));

		String ipAddress = null;
		switch (deviceName) {
		case "entry": {
			ipAddress = cameraEntranceIp;
			break;
		}
		case "corridor": {
			ipAddress = cameraCorridorIp;
			break;
		}
		}

		final HttpClient client = HttpClientBuilder.create().build();

		final HttpGet request = new HttpGet("http://" + ipAddress + "/axis-cgi/jpg/image.cgi");
		try {
			final HttpResponse response = client.execute(request);
			final int errorCode = response.getStatusLine().getStatusCode();
			if (errorCode == 200) {
				final HttpEntity entity = response.getEntity();
				final InputStream is = entity.getContent();
				final FileSystem fs = FileSystems.getDefault();
				final Path path = fs.getPath(basePath, snapshotPath);
				final Path imageName = Paths.get(path.toString(), deviceName + "_" + df.format(today) + ".jpg");
				Files.createDirectories(imageName.getParent());
				final FileOutputStream fos = new FileOutputStream(imageName.toFile());
				int inByte;
				while ((inByte = is.read()) != -1) {
					fos.write(inByte);
				}
				is.close();
				fos.close();
			}
		} catch (final IOException e) {
			log.error("error taking camera snapshot", e);
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteWallThermostatList")
	public @ResponseBody Object getRemoteWallThermostatList() {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteWallThermostatList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting remote wall thermostat list", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteRadiatorThermostatList")
	public @ResponseBody Object getRemoteRadiatorThermostatList() {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteRadiatorThermostatList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting remote radiator thermostat list", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteValveDriveList")
	public @ResponseBody Object getRemoteValveDriveList() {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteValveDriveList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting remote valve drive list", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteOutdoorWeatherSensorList")
	public @ResponseBody Object getRemoteOutdoorWeatherSensorList() {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteOutdoorWeatherSensorList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting remote outdoor weather sensor list", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/floorPlan")
	public ModelAndView showGroundFloorPlan() {
		final ModelAndView mv = new ModelAndView("floor_plan");
		return mv;
	}

	@GetMapping(value = "/getWeatherSensors")
	public @ResponseBody List<Map<String, Object>> getWeatherSensors() {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		List<Map<String, Object>> resultList = null;
		try {
			resultList = homegearService.getWeatherSensorList();
		} catch (final Throwable e) {
			log.error("error getting waether sensor list", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return resultList;
	}

	@GetMapping(value = "/getSwitches")
	public @ResponseBody List<Map<String, Object>> getSwitches() {
		List<Map<String, Object>> resultList = null;
		try {
			resultList = homegearService.getRemoteSwitchList();
		} catch (final Throwable e) {
			log.error("error getting switch list", e);
		}
		return resultList;
	}

	@GetMapping(value = "/boiler/{state}")
	public @ResponseBody ResponseWrapper setBoiler(@PathVariable final boolean state) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setBoilerState(1, state);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting boiler state", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/boiler/heat/{temperature}")
	public @ResponseBody ResponseWrapper setBoilerHeatTemperature(@PathVariable final Double temperature) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homecontrol.setTemperture(temperature);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting boiler state", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/boiler/temperature/{channel}")
	public @ResponseBody ResponseWrapper getBoilerTemperature(@PathVariable final Integer channel) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			final Double value = homegearService.getBoilerTemperature(channel).doubleValue();
			rw.setMessage("Succesfully get temperature of channel " + channel + " / " + value);
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error getting boiler temperature channel", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/light/{location}/{state}")
	public @ResponseBody ResponseWrapper setLight(@PathVariable final String location,
			@PathVariable final boolean state) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setLight(location, state);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error switchting light on location", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/lightAll/{state}")
	public @ResponseBody ResponseWrapper setLight(@PathVariable final boolean state) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setLight(state);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error switchting all lights", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/heatingmode/{type}")
	public @ResponseBody ResponseWrapper setHeatingMode(@PathVariable final String type) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setHeating(type);
			rw.setMessage("Succesfully set heating mode");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error setting heating mode", e);
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/setManualTemperature/{serialNo}/{temperature}")
	public @ResponseBody ResponseWrapper setManualTemperture(@PathVariable final String serialNo,
			@PathVariable final double temperature) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.getSmartDevicebySerialNo(serialNo, IRemoteWallThermostat.class)
					.setControlMode(HeatingMode.MANUAL, (temperature / 10));
			rw.setMessage("Succesfully set heating mode");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error setting temperature", e);
			rw.setSuccessful(false);
			rw.setMessage("setting new temperture of [" + serialNo + "] failed.");
		}

		return rw;
	}

	@GetMapping(value = "/unlockKey/{serialNo}")
	public @ResponseBody ResponseWrapper unlockKey(@PathVariable final String serialNo) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.getSmartDevicebySerialNo(serialNo, HomematicRemoteRadiatorThermostat.class).setLocked(false,
					false);
			rw.setMessage("Succesfully set heating mode");
			rw.setSuccessful(true);
		} catch (final Throwable e) {
			log.error("error unlocking thermosatat keys", e);
			rw.setSuccessful(false);
			rw.setMessage("setting new temperture of [" + serialNo + "] failed.");
		}
		return rw;
	}

	@GetMapping(value = "/motion_detection")
	public String motionDetection() {
		log.debug("message received");
		return "HTTP/1.0 200 OK\r\n\nContent-Type: text/plain\n\n\n";
	}

	@GetMapping("/getEvents")
	public @ResponseBody ResponseWrapper getEvents() {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			final List<Event> list = service.getUpcomingEventList(new String[] { "today", "tomorrow", "upcoming" });
			rw.setData(list);
			rw.setTotal(list.size());
			rw.setMessage("Succesfully got events");
			rw.setSuccessful(true);
		} catch (final Exception e) {
			log.error("error getting upcoming events", e);
			rw.setSuccessful(false);
			rw.setMessage("getting event data failed.");
		}
		return rw;
	}

	@GetMapping("/tasmotaresponse/{button}")
	public @ResponseBody ResponseWrapper tasmotaresponse(@PathVariable final String button) {
		final ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		log.info("tasmota button");
		return rw;
	}

	@GetMapping("/bot")
	public @ResponseBody ResponseWrapper bot(@RequestParam("message") String message) {
		ResponseWrapper rw = new ResponseWrapper(true, "Successfully send Telegram message");
		try {
			bot.sendMessage(message);
		} catch (Exception ex) {
			rw.setMessage("Failed to send Telegram message");
			rw.setSuccessful(false);
		}
		return rw;
	}

	// shelly 1 rule
	// rule1 on Power1#State=1 do websend [10.100.200.205:50081]
	// /homeautomation/tasmotaresponse/entry endon

	// @GetMapping("/tasmotaconfig/{button}")

	// {"Rule1":"ON","Once":"OFF","StopOnError":"OFF","Free":416,"Rules":"on
	// Power1#State=1 do websend [10.100.200.205:50081]
	// /homeautomation/tasmotaresponse/test1 endon"}

	/*
	 * public @ResponseBody ResponseWrapper tasmotaconfig(@PathVariable String
	 * button) { ResponseWrapper rw = new ResponseWrapper(false,
	 * "Failed to get data"); String command =
	 * "rule1 on Power1#State=1 do websend [10.100.200.205:50081] /homeautomation/tasmotaresponse/test1 endon"
	 * ; command = StringEscapeUtils.escapeHtml4(command); String result = null;
	 * HttpClient client = HttpClientBuilder.create().build(); HttpGet request = new
	 * HttpGet("http://10.100.200.229/cm?cmnd=" + command); try { HttpResponse
	 * response = client.execute(request); int errorCode =
	 * response.getStatusLine().getStatusCode(); if (errorCode == 200) { HttpEntity
	 * entity = response.getEntity(); result = EntityUtils.toString(entity); }
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } return rw; }
	 * 
	 * @GetMapping("/stream1")
	 * 
	 * @ResponseBody public StreamingResponseBody getVidoeStream1(@RequestParam
	 * String any) throws IOException {
	 * 
	 * RestTemplate restTemplate = new RestTemplate(); ResponseEntity<Resource>
	 * responseEntity = restTemplate.exchange(
	 * "rtsp://10.100.200.170/axis-media/media.amp?videocodec=h264", HttpMethod.GET,
	 * null, Resource.class); InputStream st =
	 * responseEntity.getBody().getInputStream(); return (os) -> { readAndWrite(st,
	 * os); };
	 * 
	 * }
	 * 
	 * private void readAndWrite(final InputStream is, OutputStream os) throws
	 * IOException { byte[] data = new byte[2048]; int read = 0; while ((read =
	 * is.read(data)) > 0) { os.write(data, 0, read); } os.flush(); }
	 */
}
