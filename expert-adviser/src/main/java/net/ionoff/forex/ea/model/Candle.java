package net.ionoff.forex.ea.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@MappedSuperclass
public abstract class Candle implements Entity {
	
	private static final long serialVersionUID = 1L;
	
	public enum PERIOD {
		M1(1), M5(2), M15(3), M30(4), H1(5), H4(6), D1(7), W1(8), MN1(9);
		
		private final int value;
		
		PERIOD(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public static PERIOD parse(int period) {
			for (PERIOD p : PERIOD.values()) {
				if (p.getValue() == period) {
					return p;
				}
			}
			throw new IllegalArgumentException("Invalid period number value: " + period);
		}
		
		public static String getName(int period) {
			return parse(period).toString();
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "time_")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	private double high;
	private double low;
	private double open;
	private double close;
	private long volume;
	private long volBuy;
	private long volSell;

	@Transient
	private Calendar calendar;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	
	public double getUpperShadow() {
		if (isBull()) {
			return high - close;
		}
		else return high - open;
	}
	
	public double getLowerShadow() {
		if (isBear()) {
			return low - close;
		}
		else return low - open;
	}
	
	public boolean isBear() {
		return close < open;
	}
	
	public boolean isBull() {
		return close > open;
	}
	
	public boolean isDojis() {
		return close == open;
	}
	
	public double getBody() {
		if (isBull()) {
			return close - open;
		}
		else {
			return open - close;
		}
	}

	public long getVolSell() {
		return volSell;
	}
	public void setVolSell(long volSell) {
		this.volSell = volSell;
	}

	public long getVolBuy() {
		return volBuy;
	}
	public void setVolBuy(long volBuy) {
		this.volBuy = volBuy;
	}


	public double getHeigh() {
		return high - low;
	}
	
	public Calendar getCalendar() {
		if (time == null) {
			calendar = null;
			return null;
		}
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendar.setTime(time);
		}
		return calendar;
	}
	
	public boolean isInEuUsSession() {
		int hour = getCalendar().get(Calendar.HOUR_OF_DAY);
		return hour >= 9 || hour < 2;
	}
	
	public int getPeriod() {
		return 0;
	}
}
