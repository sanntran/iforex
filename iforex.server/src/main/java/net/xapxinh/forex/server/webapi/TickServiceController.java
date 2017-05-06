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

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Symbol;
import net.xapxinh.forex.server.entity.candle.EurUsdD1Candle;
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

			@RequestParam("m15BarTime") String m15BarTime, @RequestParam("m15BarOpen") double m15BarOpen,
			@RequestParam("m15BarHigh") double m15BarHigh, @RequestParam("m15BarLow") double m15BarLow,
			@RequestParam("m15BarClose") double m15BarClose, @RequestParam("m15BarVolume") long m15BarVolume,
			
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
			saveEurUsdM5Candle(m5BarTime, m5BarOpen, m5BarHigh, m5BarLow, m5BarClose, m5BarVolume);
			saveEurUsdM15Candle(m15BarTime, m15BarOpen, m15BarHigh, m15BarLow, m15BarClose, m15BarVolume);
			saveEurUsdM30Candle(m30BarTime, m30BarOpen, m30BarHigh, m30BarLow, m30BarClose, m30BarVolume);
			saveEurUsdH1Candle(h1BarTime, h1BarOpen, h1BarHigh, h1BarLow, h1BarClose, h1BarVolume);
			saveEurUsdH4Candle(h4BarTime, h4BarOpen, h4BarHigh, h4BarLow, h4BarClose, h4BarVolume);
			saveEurUsdD1Candle(d1BarTime, d1BarOpen, d1BarHigh, d1BarLow, d1BarClose, d1BarVolume);
		}

		return "200";
	}
	
	private void saveEurUsdD1Candle(String d1BarTime, double d1BarOpen, double d1BarHigh, double d1BarLow,
			double d1BarClose, long d1BarVolume) throws ParseException {
		Date d1BarDate = MT4_DATE_FORMAT.parse(d1BarTime);
		EurUsdD1Candle eurUsdD1Candle = getEurUsdD1Candle(d1BarDate);
		updateCandle(eurUsdD1Candle, d1BarOpen, d1BarHigh, d1BarLow, d1BarClose, d1BarVolume, d1BarDate);
		candleService.save(eurUsdD1Candle);
	}
	
	private void saveEurUsdH4Candle(String h4BarTime, double h4BarOpen, double h4BarHigh, double h4BarLow,
			double h4BarClose, long h4BarVolume) throws ParseException {
		Date h4BarDate = MT4_DATE_FORMAT.parse(h4BarTime);
		EurUsdD1Candle eurUsdH4Candle = getEurUsdH4Candle(h4BarDate);
		updateCandle(eurUsdH4Candle, h4BarOpen, h4BarHigh, h4BarLow, h4BarClose, h4BarVolume, h4BarDate);
		candleService.save(eurUsdH4Candle);
	}

	private void saveEurUsdH1Candle(String h1BarTime, double h1BarOpen, double h1BarHigh, double h1BarLow,
			double h1BarClose, long h1BarVolume) throws ParseException {
		Date h1BarDate = MT4_DATE_FORMAT.parse(h1BarTime);
		EurUsdH1Candle eurUsdH1Candle = getEurUsdH1Candle(h1BarDate);
		updateCandle(eurUsdH1Candle, h1BarOpen, h1BarHigh, h1BarLow, h1BarClose, h1BarVolume, h1BarDate);
		candleService.save(eurUsdH1Candle);
	}

	private void saveEurUsdM30Candle(String m30BarTime, double m30BarOpen, double m30BarHigh, double m30BarLow,
			double m30BarClose, long m30BarVolume) throws ParseException {
		Date m30BarDate = MT4_DATE_FORMAT.parse(m30BarTime);
		EurUsdM30Candle eurUsdM30Candle = getEurUsdM30Candle(m30BarDate);
		updateCandle(eurUsdM30Candle, m30BarOpen, m30BarHigh, m30BarLow, m30BarClose, m30BarVolume, m30BarDate);
		candleService.save(eurUsdM30Candle);
	}

	private void saveEurUsdM15Candle(String m15BarTime, double m15BarOpen, double m15BarHigh, double m15BarLow,
			double m15BarClose, long m15BarVolume) throws ParseException {
		Date m15BarDate = MT4_DATE_FORMAT.parse(m15BarTime);
		EurUsdM15Candle eurUsdM15Candle = getEurUsdM15Candle(m15BarDate);
		updateCandle(eurUsdM15Candle, m15BarOpen, m15BarHigh, m15BarLow, m15BarClose, m15BarVolume, m15BarDate);
		candleService.save(eurUsdM15Candle);
	}
	
	private void saveEurUsdM5Candle(String m5BarTime, double m5BarOpen, double m5BarHigh, double m5BarLow,
			double m5BarClose, long m5BarVolume) throws ParseException {
		Date m5BarDate = MT4_DATE_FORMAT.parse(m5BarTime);
		EurUsdM5Candle eurUsdM5Candle = getEurUsdM5Candle(m5BarDate);
		updateCandle(eurUsdM5Candle, m5BarOpen, m5BarHigh, m5BarLow, m5BarClose, m5BarVolume, m5BarDate);
		candleService.save(eurUsdM5Candle);
	}
	
	private void updateCandle(Candle candle, double open, double high, double low, double close,
			long volume, Date date) {
		candle.setTime(date);
		candle.setLow(low);
		candle.setHigh(high);
		candle.setOpen(open);
		if ((candle.isNew() && close > candle.getOpen())
				|| (!candle.isNew() && close > candle.getClose())) {
			candle.setVolbuy(candle.getVolbuy() + 1);
		}
		candle.setClose(close);		
		candle.setVolume(volume);
	}
	
	private EurUsdD1Candle getEurUsdD1Candle(Date d1BarTime) throws ParseException {
		EurUsdD1Candle eurUsdD1Candle = candleService.findByTime(d1BarTime, EurUsdD1Candle.class);
		if (eurUsdD1Candle == null) {
			eurUsdD1Candle = new EurUsdD1Candle();
		}
		return eurUsdD1Candle;
	}

	private EurUsdD1Candle getEurUsdH4Candle(Date h4BarTime) throws ParseException {
		EurUsdD1Candle eurUsdH4Candle = candleService.findByTime(h4BarTime, EurUsdD1Candle.class);
		if (eurUsdH4Candle == null) {
			eurUsdH4Candle = new EurUsdD1Candle();
		}
		return eurUsdH4Candle;
	}

	private EurUsdH1Candle getEurUsdH1Candle(Date h1BarTime) throws ParseException {
		EurUsdH1Candle eurUsdH1Candle = candleService.findByTime(h1BarTime, EurUsdH1Candle.class);
		if (eurUsdH1Candle == null) {
			eurUsdH1Candle = new EurUsdH1Candle();
		}
		return eurUsdH1Candle;
	}

	private EurUsdM30Candle getEurUsdM30Candle(Date m30BarTime) throws ParseException {
		EurUsdM30Candle eurUsdM30Candle = candleService.findByTime(m30BarTime, EurUsdM30Candle.class);
		if (eurUsdM30Candle == null) {
			eurUsdM30Candle = new EurUsdM30Candle();
		}
		return eurUsdM30Candle;
	}

	private EurUsdM15Candle getEurUsdM15Candle(Date m15BarTime) throws ParseException {
		EurUsdM15Candle eurUsdM15Candle = candleService.findByTime(m15BarTime, EurUsdM15Candle.class);
		if (eurUsdM15Candle == null) {
			eurUsdM15Candle = new EurUsdM15Candle();
		}
		return eurUsdM15Candle;
	}
	
	private EurUsdM5Candle getEurUsdM5Candle(Date m5BarTime) throws ParseException {

		EurUsdM5Candle eurUsdM5Candle = candleService.findByTime(m5BarTime, EurUsdM5Candle.class);
		if (eurUsdM5Candle == null) {
			eurUsdM5Candle = new EurUsdM5Candle();
		}
		return eurUsdM5Candle;
	}
	
}
