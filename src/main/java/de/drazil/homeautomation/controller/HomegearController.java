package de.drazil.homeautomation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@GetMapping("/polling")
	public String polling() {
		return "polling";
	}

	@GetMapping("/floorplan")
	public String floorplan() {
		return "floorplan";
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

	@RequestMapping(value = "/getRemoteRadiatorThermostatList", method = RequestMethod.GET)
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

	@RequestMapping(value = "/getRemoteOutdoorWeatherSensorList", method = RequestMethod.GET)
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

	@RequestMapping(value = "/floorPlan", method = RequestMethod.GET)
	public ModelAndView showGroundFloorPlan() {
		ModelAndView mv = new ModelAndView("floor_plan");
		return mv;
	}

	@RequestMapping(value = "/getWeatherSensors", method = { RequestMethod.GET })
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

	@RequestMapping(value = "/getSwitches", method = { RequestMethod.GET })
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

	@RequestMapping("/getEvents")
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
}
