package net.xapxinh.forex.server.webapi;

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
public class DecisionServiceController {

	private static final  Logger LOGGER = Logger.getLogger(DecisionServiceController.class.getName());
	
	@RequestMapping(
			value = "/decisions",
			method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getDecisions(
			@RequestParam("symbol") String symbol,
			@RequestParam(name="ticktime", required=false) String tickTime,
			@RequestParam(name="tickbid", required=false) double tickBid,
			@RequestParam(name="tickask", required=false) double tickAsk,
			@RequestParam(name="ticklast", required=false) double tickLast,
			@RequestParam(name="error", required=false) String error,
			@RequestParam("bartime") String barTime,
			@RequestParam("baropen") String barOpen,
			@RequestParam("barhigh") String barHigh,
			@RequestParam("barlow") String barLow,
			@RequestParam("barclose") String barClose,
			@RequestParam("barvolume") String barVolume,
			HttpServletRequest request) {
		
		System.out.println(
				"Symbol: " + symbol + 
				" Time: " + tickTime  + 
				" Price: " + tickLast +
				" barTime: " + barTime + 
				" barOpen: " + barOpen + 
				" barHigh: " + barHigh + 
				" barLow: " + barLow + 
				" barClose: " + barClose + 
				" barVolume: " + barVolume);
		return "200";
	}
}
