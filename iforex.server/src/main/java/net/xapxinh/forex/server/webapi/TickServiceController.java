package net.xapxinh.forex.server.webapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import net.xapxinh.forex.server.entity.Symbol;
import net.xapxinh.forex.server.entity.candle.EurUsdH1Candle;
import net.xapxinh.forex.server.entity.candle.EurUsdM15Candle;
import net.xapxinh.forex.server.entity.candle.EurUsdM30Candle;
import net.xapxinh.forex.server.entity.candle.EurUsdM5Candle;
import net.xapxinh.forex.server.persistence.service.ICandleService;

@RestController
@EnableWebMvc
public class TickServiceController {

	private static final Logger LOGGER = Logger.getLogger(TickServiceController.class.getName());
	private static final SimpleDateFormat MT4_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	@Autowired
	private ICandleService candleService;

	@RequestMapping(value = "/ticks", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String onTick(@RequestParam("symbol") String symbol,
			@RequestParam(name = "ticktime", required = false) String tickTime,
			@RequestParam(name = "tickbid", required = false) double tickBid,
			@RequestParam(name = "tickask", required = false) double tickAsk,
			@RequestParam(name = "ticklast", required = false) double tickLast,
			@RequestParam(name = "error", required = false) String error,

			@RequestParam("m5BarTime") String m5BarTime, @RequestParam("m5BarOpen") double m5BarOpen,
			@RequestParam("m5BarHigh") double m5BarHigh, @RequestParam("m5BarLow") double m5BarLow,
			@RequestParam("m5BarClose") double m5BarClose, @RequestParam("m5BarVolume") long m5BarVolume,

			@RequestParam("m30BarTime") String m30BarTime, @RequestParam("m30BarOpen") double m30BarOpen,
			@RequestParam("m30BarHigh") double m30BarHigh, @RequestParam("m30BarLow") double m30BarLow,
			@RequestParam("m30BarClose") double m30BarClose, @RequestParam("m30BarVolume") long m30BarVolume,

			@RequestParam("h1BarTime") String h1BarTime, @RequestParam("h1BarOpen") double h1BarOpen,
			@RequestParam("h1BarHigh") double h1BarHigh, @RequestParam("h1BarLow") double h1BarLow,
			@RequestParam("h1BarClose") double h1BarClose, @RequestParam("h1BarVolume") long h1BarVolume,

			@RequestParam("h4BarTime") String h4BarTime, @RequestParam("h4BarOpen") double h4BarOpen,
			@RequestParam("h4BarHigh") double h4BarHigh, @RequestParam("h4BarLow") double h4BarLow,
			@RequestParam("h4BarClose") double h4BarClose, @RequestParam("h4BarVolume") long h4BarVolume,

			@RequestParam("d1BarTime") String d1BarTime, @RequestParam("d1BarOpen") double d1BarOpen,
			@RequestParam("d1BarHigh") double d1BarHigh, @RequestParam("d1BarLow") double d1BarLow,
			@RequestParam("d1BarClose") double d1BarClose, @RequestParam("d1BarVolume") long d1BarVolume,

			HttpServletRequest request) throws ParseException {

		System.out.println("Symbol: " + symbol + " Time: " + tickTime + " Price: " + tickLast + " m5BarTime: "
				+ m5BarTime + " m5BarOpen: " + m5BarOpen + " m5BarHigh: " + m5BarHigh + " m5BarLow: " + m5BarLow
				+ " m5BarClose: " + m5BarClose + " m5BarVolume: " + m5BarVolume);

		if (Symbol.EURUSD.equals(symbol)) {
			Date m5BarDate = MT4_DATE_FORMAT.parse(m5BarTime);

			EurUsdM5Candle eurUsdM5Candle = getEurUsdM5Candle(m5BarDate);
			eurUsdM5Candle.setTime(m5BarDate);
			eurUsdM5Candle.setLow(m5BarLow);
			eurUsdM5Candle.setHigh(m5BarHigh);
			eurUsdM5Candle.setOpen(m5BarOpen);
			eurUsdM5Candle.setClose(m5BarClose);
			eurUsdM5Candle.setVolume(m5BarVolume);
			candleService.save(eurUsdM5Candle);
		}

		return "200";
	}

	private EurUsdM5Candle getEurUsdM5Candle(Date m5BarTime) throws ParseException {

		EurUsdM5Candle eurUsdM5Candle = candleService.findByTime(m5BarTime, EurUsdM5Candle.class);
		if (eurUsdM5Candle == null) {
			eurUsdM5Candle = new EurUsdM5Candle();
		}
		return eurUsdM5Candle;
	}

	private EurUsdM15Candle getEurUsdM15Candle(Date m15BarTime) throws ParseException {

		EurUsdM15Candle eurUsdM15Candle = candleService.findByTime(m15BarTime, EurUsdM15Candle.class);
		if (eurUsdM15Candle == null) {
			eurUsdM15Candle = new EurUsdM15Candle();
		}
		return eurUsdM15Candle;
	}

	private EurUsdM30Candle getEurUsdM30Candle(Date m30BarTime) throws ParseException {

		EurUsdM30Candle eurUsdM30Candle = candleService.findByTime(m30BarTime, EurUsdM30Candle.class);
		if (eurUsdM30Candle == null) {
			eurUsdM30Candle = new EurUsdM30Candle();
		}
		return eurUsdM30Candle;
	}

	private EurUsdH1Candle getEurUsdMH1Candle(Date m15BarTime) throws ParseException {

		EurUsdH1Candle eurUsdH1Candle = candleService.findByTime(m15BarTime, EurUsdH1Candle.class);
		if (eurUsdH1Candle == null) {
			eurUsdH1Candle = new EurUsdH1Candle();
		}
		return eurUsdH1Candle;
	}
}
