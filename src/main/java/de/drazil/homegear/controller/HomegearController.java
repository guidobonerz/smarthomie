package de.drazil.homegear.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.drazil.homegear.IHeatingDevice.HeatingMode;
import de.drazil.homegear.IRemoteWallThermostat;
import de.drazil.homegear.bean.ResponseWrapper;

import de.drazil.homegear.service.HomegearService;

@Controller
public class HomegearController {
	@Autowired
	private HomegearService homegearService;
	
	
	@RequestMapping(value = "/x", method = RequestMethod.GET)
	public ModelAndView show() {

		ModelAndView mv = new ModelAndView("homegear");
		try {
			mv.addObject("remoteRadiatorThermostatList", homegearService.getRemoteRadiatorThermostatList());
			mv.addObject("remoteWallThermostatList", homegearService.getRemoteWallThermostatList());
			mv.addObject("remoteOutdoorWeatherSensorList", homegearService.getRemoteOutdoorWeatherSensorList());
			mv.addObject("remoteSwitchList", homegearService.getRemoteSwitchList());

		} catch (Throwable e) {

			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = "/getRemoteWallThermostatList", method = RequestMethod.GET)
	public @ResponseBody Object getRemoteWallThermostatList() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
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
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		try {
			rw.setData(homegearService.getRemoteRadiatorThermostatList());
			rw.setMessage("Succesfully got data");
			rw.setSuccessful(true);
		} catch (Throwable e) {
			rw.setData(null);
			rw.setSuccessful(false);
			rw.setMessage(e.getMessage());
		}
		return rw;
	}

	@RequestMapping(value = "/getRemoteValveDriveList", method = RequestMethod.GET)
	public @ResponseBody Object getRemoteValveDriveList() {
		ResponseWrapper rw = new ResponseWrapper(false, "Failed to get data");
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
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
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
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

	@RequestMapping(value = "/setManualTemperature/{serialNo}/{temperature}", method = { RequestMethod.GET })
	public @ResponseBody ResponseWrapper setManualTemperture(@PathVariable String serialNo,
			@PathVariable double temperature) {
		ResponseWrapper response = new ResponseWrapper();

		try {
			homegearService.getSmartDevicebySerialNo(serialNo, IRemoteWallThermostat.class)
					.setControlMode(HeatingMode.MANUAL, (temperature / 10));
			response.setSuccessful(true);
		} catch (Throwable e) {
			response.setSuccessful(false);
			response.setMessage("setting new temperture of [" + serialNo + "] failed.");
		}

		return response;
	}
}
