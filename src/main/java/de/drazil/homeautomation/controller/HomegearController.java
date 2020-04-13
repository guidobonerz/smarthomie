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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.drazil.homeautomation.bean.ResponseWrapper;
import de.drazil.homeautomation.dto.Event;
import de.drazil.homeautomation.service.ExternalSchedulerService;
import de.drazil.homeautomation.service.HomegearService;
import de.drazil.homeautomation.service.Message;
import de.drazil.homeautomation.service.MessageService;
import de.drazil.homeautomation.smartdevices.IHeatingDevice.HeatingMode;
import de.drazil.homeautomation.smartdevices.IRemoteWallThermostat;
import de.drazil.homeautomation.smartdevicesimpl.homematic.HomematicRemoteRadiatorThermostat;

@Controller
public class HomegearController {
	@Autowired
	private HomegearService homegearService;

	@Autowired
	MessageService messageService;

	@Autowired
	private ExternalSchedulerService service;

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
	public String dashboard(Model model) {
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
	public ResponseWrapper snapshot(@RequestParam(value = "deviceName", required = true) String deviceName) {
		ResponseWrapper rw = new ResponseWrapper(false, "snapshot successfully taken");

		String pattern = "yyyyddMM-HHmmss";
		DateFormat df = new SimpleDateFormat(pattern);
		Date today = Calendar.getInstance().getTime();

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

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet("http://" + ipAddress + "/axis-cgi/jpg/image.cgi");
		try {
			HttpResponse response = client.execute(request);
			int errorCode = response.getStatusLine().getStatusCode();
			if (errorCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				FileSystem fs = FileSystems.getDefault();
				Path path = fs.getPath(basePath, snapshotPath);
				Path imageName = Paths.get(path.toString(), deviceName + "_" + df.format(today) + ".jpg");
				Files.createDirectories(imageName.getParent());
				FileOutputStream fos = new FileOutputStream(imageName.toFile());
				int inByte;
				while ((inByte = is.read()) != -1) {
					fos.write(inByte);
				}
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteWallThermostatList")
	public @ResponseBody Object getRemoteWallThermostatList() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteWallThermostatList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteRadiatorThermostatList")
	public @ResponseBody Object getRemoteRadiatorThermostatList() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteRadiatorThermostatList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@RequestMapping(value = "/getRemoteValveDriveList", method = RequestMethod.GET)
	public @ResponseBody Object getRemoteValveDriveList() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteValveDriveList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/getRemoteOutdoorWeatherSensorList")
	public @ResponseBody Object getRemoteOutdoorWeatherSensorList() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			rw.setData(homegearService.getRemoteOutdoorWeatherSensorList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/floorPlan")
	public ModelAndView showGroundFloorPlan() {
		ModelAndView mv = new ModelAndView("floor_plan");
		return mv;
	}

	@GetMapping(value = "/getWeatherSensors")
	public @ResponseBody List<Map<String, Object>> getWeatherSensors() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		List<Map<String, Object>> resultList = null;
		try {
			resultList = homegearService.getWeatherSensorList();
		} catch (Throwable e) {
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
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping(value = "/boiler/{state}")
	public @ResponseBody ResponseWrapper setBoiler(@PathVariable boolean state) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setBoilerState(1, state);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/boiler/temperature/{channel}")
	public @ResponseBody ResponseWrapper getBoilerTemperature(@PathVariable Integer channel) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			Double value = homegearService.getBoilerTemperature(channel).doubleValue();
			rw.setMessage("Succesfully get temperature of channel " + channel + " / " + value);
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/light/{location}/{state}")
	public @ResponseBody ResponseWrapper setLight(@PathVariable String location, @PathVariable boolean state) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setLight(location, state);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/lightAll/{state}")
	public @ResponseBody ResponseWrapper setLight(@PathVariable boolean state) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setLight(state);
			rw.setMessage("Succesfully set state");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/heatingmode/{type}")
	public @ResponseBody ResponseWrapper setHeatingMode(@PathVariable String type) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.setHeating(type);
			rw.setMessage("Succesfully set heating mode");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@GetMapping(value = "/setManualTemperature/{serialNo}/{temperature}")
	public @ResponseBody ResponseWrapper setManualTemperture(@PathVariable String serialNo,
			@PathVariable double temperature) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.getSmartDevicebySerialNo(serialNo, IRemoteWallThermostat.class)
					.setControlMode(HeatingMode.MANUAL, (temperature / 10));
			rw.setMessage("Succesfully set heating mode");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setSuccessful(false);
			rw.setMessage("setting new temperture of [" + serialNo + "] failed.");
		}

		return rw;
	}

	@GetMapping(value = "/unlockKey/{serialNo}")
	public @ResponseBody ResponseWrapper unlockKey(@PathVariable String serialNo) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			homegearService.getSmartDevicebySerialNo(serialNo, HomematicRemoteRadiatorThermostat.class).setLocked(false,
					false);
			rw.setMessage("Succesfully set heating mode");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setSuccessful(false);
			rw.setMessage("setting new temperture of [" + serialNo + "] failed.");
		}
		return rw;
	}

	@GetMapping(value = "/motion_detection")
	public String motionDetection() {

		System.out.println("message received");

		return new String("HTTP/1.0 200 OK\r\n\n" + "Content-Type: text/plain\n\n" + "\n");
	}

	@GetMapping(value = "/getMessages")
	public @ResponseBody ResponseWrapper getMessages() {

		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			List<Message> list = messageService.getMessageList();
			rw.setData(list);
			rw.setTotal(list.size());
			rw.setMessage("Succesfully got messages");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setSuccessful(false);
			rw.setMessage("getting event data failed.");
		}
		return rw;
	}

	@GetMapping("/getEvents")
	public @ResponseBody ResponseWrapper getEvents() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		try {
			List<Event> list = service.getUpcomingEventList(new String[] { "today", "tomorrow", "upcoming" });
			rw.setData(list);
			rw.setTotal(list.size());
			rw.setMessage("Succesfully got events");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			e.printStackTrace();
			rw.setSuccessful(false);
			rw.setMessage("getting event data failed.");
		}
		return rw;
	}

	@GetMapping("/tasmotaresponse/{button}")
	public @ResponseBody ResponseWrapper tasmotaresponse(@PathVariable String button) {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		System.out.println(button + " activated");
		return rw;
	}
	/*
	 * @GetMapping("/bot") public @ResponseBody ResponseWrapper
	 * bot(@RequestParam("message") String message) { ResponseWrapper rw = new
	 * ResponseWrapper(false, "Failed to get data");
	 * System.out.println("send message to bot :" + message);
	 * bot.sendMessage(message); return rw; }
	 */
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
