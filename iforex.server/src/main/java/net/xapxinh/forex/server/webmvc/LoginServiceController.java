package net.xapxinh.forex.server.webmvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class LoginServiceController {

	private static final  Logger LOGGER = Logger.getLogger(LoginServiceController.class.getName());
	
	@RequestMapping(
			value = "/decisions",
			method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	@ResponseBody
	public String geDecisions(
			@RequestParam("value") String value,
			@RequestParam("time") String time,
			HttpServletRequest request) {
		LOGGER.info("Value: " + value + " Time: " + time);
		return value;
	}
}
