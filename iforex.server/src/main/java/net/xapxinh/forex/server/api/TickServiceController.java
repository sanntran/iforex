package net.xapxinh.forex.server.api;

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
import net.xapxinh.forex.server.entity.Decision;
import net.xapxinh.forex.server.entity.candle.D1Candle;
import net.xapxinh.forex.server.entity.candle.H1Candle;
import net.xapxinh.forex.server.entity.candle.M15Candle;
import net.xapxinh.forex.server.entity.candle.M1Candle;
import net.xapxinh.forex.server.entity.candle.M30Candle;
import net.xapxinh.forex.server.entity.candle.M5Candle;
import net.xapxinh.forex.server.persistence.service.ICandleService;
import net.xapxinh.forex.server.strategy.IStrategist;

@RestController
@EnableWebMvc
public class TickServiceController {

	private static final Logger LOGGER = Logger.getLogger(TickServiceController.class.getName());
	private static final SimpleDateFormat MT4_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	@Autowired
	private ICandleService candleService;
	
	@Autowired
	private IStrategist strategist;	

	@RequestMapping(value = "/ticks", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String onTick(@RequestParam("symbol") String symbol,
			@RequestParam(name = "ticktime", required = false) String tickTime,
			@RequestParam(name = "tickbid", required = false) double tickBid,
			@RequestParam(name = "tickask", required = false) double tickAsk,
			@RequestParam(name = "ticklast", required = false) double tickLast,
			@RequestParam(name = "error", required = false) String error,
			
			@RequestParam("m1BarTime") String m1BarTime, @RequestParam("m1BarOpen") double m1BarOpen,
			@RequestParam("m1BarHigh") double m1BarHigh, @RequestParam("m1BarLow") double m1BarLow,
			@RequestParam("m1BarClose") double m1BarClose, @RequestParam("m1BarVolume") long m1BarVolume,

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
		
		saveM1Candle(m1BarTime, m1BarOpen, m1BarHigh, m1BarLow, m1BarClose, m1BarVolume);
		saveM5Candle(m5BarTime, m5BarOpen, m5BarHigh, m5BarLow, m5BarClose, m5BarVolume);
		saveM15Candle(m15BarTime, m15BarOpen, m15BarHigh, m15BarLow, m15BarClose, m15BarVolume);
		saveM30Candle(m30BarTime, m30BarOpen, m30BarHigh, m30BarLow, m30BarClose, m30BarVolume);
		saveH1Candle(h1BarTime, h1BarOpen, h1BarHigh, h1BarLow, h1BarClose, h1BarVolume);
		saveH4Candle(h4BarTime, h4BarOpen, h4BarHigh, h4BarLow, h4BarClose, h4BarVolume);
		saveD1Candle(d1BarTime, d1BarOpen, d1BarHigh, d1BarLow, d1BarClose, d1BarVolume);

		return toMqlFormat(strategist.makeDecision());
	}
	
	private String toMqlFormat(Decision decision) {
		// TODO Auto-generated method stub
		return null;
	}

	private void saveD1Candle(String d1BarTime, double d1BarOpen, double d1BarHigh, double d1BarLow,
			double d1BarClose, long d1BarVolume) throws ParseException {
		Date d1BarDate = MT4_DATE_FORMAT.parse(d1BarTime);
		D1Candle d1Candle = getD1Candle(d1BarDate);
		updateCandle(d1Candle, d1BarOpen, d1BarHigh, d1BarLow, d1BarClose, d1BarVolume, d1BarDate);
		candleService.save(d1Candle);
	}
	
	private void saveH4Candle(String h4BarTime, double h4BarOpen, double h4BarHigh, double h4BarLow,
			double h4BarClose, long h4BarVolume) throws ParseException {
		Date h4BarDate = MT4_DATE_FORMAT.parse(h4BarTime);
		D1Candle h4Candle = getH4Candle(h4BarDate);
		updateCandle(h4Candle, h4BarOpen, h4BarHigh, h4BarLow, h4BarClose, h4BarVolume, h4BarDate);
		candleService.save(h4Candle);
	}

	private void saveH1Candle(String h1BarTime, double h1BarOpen, double h1BarHigh, double h1BarLow,
			double h1BarClose, long h1BarVolume) throws ParseException {
		Date h1BarDate = MT4_DATE_FORMAT.parse(h1BarTime);
		H1Candle h1Candle = getH1Candle(h1BarDate);
		updateCandle(h1Candle, h1BarOpen, h1BarHigh, h1BarLow, h1BarClose, h1BarVolume, h1BarDate);
		candleService.save(h1Candle);
	}

	private void saveM30Candle(String m30BarTime, double m30BarOpen, double m30BarHigh, double m30BarLow,
			double m30BarClose, long m30BarVolume) throws ParseException {
		Date m30BarDate = MT4_DATE_FORMAT.parse(m30BarTime);
		M30Candle m30Candle = getM30Candle(m30BarDate);
		updateCandle(m30Candle, m30BarOpen, m30BarHigh, m30BarLow, m30BarClose, m30BarVolume, m30BarDate);
		candleService.save(m30Candle);
	}

	private void saveM15Candle(String m15BarTime, double m15BarOpen, double m15BarHigh, double m15BarLow,
			double m15BarClose, long m15BarVolume) throws ParseException {
		Date m15BarDate = MT4_DATE_FORMAT.parse(m15BarTime);
		M15Candle m15Candle = getM15Candle(m15BarDate);
		updateCandle(m15Candle, m15BarOpen, m15BarHigh, m15BarLow, m15BarClose, m15BarVolume, m15BarDate);
		candleService.save(m15Candle);
	}
	
	private void saveM5Candle(String m5BarTime, double m5BarOpen, double m5BarHigh, double m5BarLow,
			double m5BarClose, long m5BarVolume) throws ParseException {
		Date m5BarDate = MT4_DATE_FORMAT.parse(m5BarTime);
		M5Candle m5Candle = getM5Candle(m5BarDate);
		updateCandle(m5Candle, m5BarOpen, m5BarHigh, m5BarLow, m5BarClose, m5BarVolume, m5BarDate);
		candleService.save(m5Candle);
	}
	
	private void saveM1Candle(String m1BarTime, double m1BarOpen, double m1BarHigh, double m1BarLow,
			double m1BarClose, long m1BarVolume) throws ParseException {
		Date m1BarDate = MT4_DATE_FORMAT.parse(m1BarTime);
		M1Candle m1Candle = getM1Candle(m1BarDate);
		updateCandle(m1Candle, m1BarOpen, m1BarHigh, m1BarLow, m1BarClose, m1BarVolume, m1BarDate);
		candleService.save(m1Candle);
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
	
	private D1Candle getD1Candle(Date d1BarTime) throws ParseException {
		D1Candle d1Candle = candleService.findByTime(d1BarTime, D1Candle.class);
		if (d1Candle == null) {
			d1Candle = new D1Candle();
		}
		return d1Candle;
	}

	private D1Candle getH4Candle(Date h4BarTime) throws ParseException {
		D1Candle h4Candle = candleService.findByTime(h4BarTime, D1Candle.class);
		if (h4Candle == null) {
			h4Candle = new D1Candle();
		}
		return h4Candle;
	}

	private H1Candle getH1Candle(Date h1BarTime) throws ParseException {
		H1Candle h1Candle = candleService.findByTime(h1BarTime, H1Candle.class);
		if (h1Candle == null) {
			h1Candle = new H1Candle();
		}
		return h1Candle;
	}

	private M30Candle getM30Candle(Date m30BarTime) throws ParseException {
		M30Candle m30Candle = candleService.findByTime(m30BarTime, M30Candle.class);
		if (m30Candle == null) {
			m30Candle = new M30Candle();
		}
		return m30Candle;
	}

	private M15Candle getM15Candle(Date m15BarTime) throws ParseException {
		M15Candle m15Candle = candleService.findByTime(m15BarTime, M15Candle.class);
		if (m15Candle == null) {
			m15Candle = new M15Candle();
		}
		return m15Candle;
	}
	
	private M5Candle getM5Candle(Date m5BarTime) throws ParseException {
		M5Candle m5Candle = candleService.findByTime(m5BarTime, M5Candle.class);
		if (m5Candle == null) {
			m5Candle = new M5Candle();
		}
		return m5Candle;
	}

	private M1Candle getM1Candle(Date m1BarTime) throws ParseException {
		M1Candle m1Candle = candleService.findByTime(m1BarTime, M1Candle.class);
		if (m1Candle == null) {
			m1Candle = new M1Candle();
		}
		return m1Candle;
	}
}
