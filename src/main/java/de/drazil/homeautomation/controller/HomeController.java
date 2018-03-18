package de.drazil.homeautomation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	/*
	 * @RequestMapping(value = "/motion_detection", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public void motionDetection(@RequestParam(value =
	 * "location", required = true) String location) { System.out.println(location);
	 * }
	 */

	// ResponseEntity<String>
	@RequestMapping(value = "/motion_detection")
	public void event(@RequestParam(value = "location") String location,
			@RequestParam(value = "Message") String message) {

		System.out.println(location);
		System.out.println(message);
		/*
		 * try {
		 * 
		 * saveUploadedFiles(Arrays.asList(uploadfile));
		 * 
		 * } catch (IOException e) { return new
		 * ResponseEntity<>(HttpStatus.BAD_REQUEST); }
		 */
		// StringBuilder sb = new StringBuilder();
		// sb.append("location=" + location + "\r\n");
		// sb.append("Message=" + message + "\r\n");
		// return new ResponseEntity<String>("OK\r\n", HttpStatus.OK);

	}

}
