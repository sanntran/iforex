package net.xapxinh.forex.server.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class Candle extends Pojo {
	
	private static final long serialVersionUID = 1L;
	
	public enum PERIOD {
		M1(1), M5(2), M15(3), M30(4), H1(5), H4(6), D1(7), W1(8), MN1(9);
		
		private final int value;
		
		private PERIOD(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public static PERIOD parse(int period) {
			if (M1.getValue() == period) {
				return M1;
			}
			if (M5.getValue() == period) {
				return M5;
			}
			if (M15.getValue() == period) {
				return M15;
			}
			if (M30.getValue() == period) {
				return M30;
			}
			if (H1.getValue() == period) {
				return H1;
			}
			if (H4.getValue() == period) {
				return H4;
			}
			if (D1.getValue() == period) {
				return D1;
			}
			throw new NumberFormatException("Invalid period number value: " + period);
		}
		
		public static String getName(int period) {
			return parse(period).toString();
		}
	}
	
	private Date time;
	private double high;
	private double low;
	private double open;
	private double close;
	private long volume;
	private long volbuy;
	private Calendar calendar;
	private List<Wave> waves;
	
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

	public long getVolsell() {
		return volume - volbuy;
	}
	
	public long getVolbuy() {
		return volbuy;
	}
	public void setVolbuy(long volbuy) {
		this.volbuy = volbuy;
	}
	
	public List<Wave> getWaves() {
		return waves;
	}
	public void setWaves(List<Wave> waves) {
		this.waves = waves;
	}
	
	public synchronized void addWave(Wave wave) {
		if (waves == null) {
			waves = new ArrayList<>();
		}
		waves.add(wave);
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
	
	public void removeWave(Wave wave) {
		if (waves == null || waves.isEmpty()) {
			return;
		}
		for (Wave w : waves) {
			if (w.getId() == wave.getId()) {
				waves.remove(w);
				break;
			}
		}
	}

	public abstract Wave newWave();
	
	public abstract Class<? extends Wave> getWaveClass();
}
