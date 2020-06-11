package net.ionoff.forex.ea.entity;

import net.ionoff.forex.ea.constant.Period;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@MappedSuperclass
public class Candle implements Entity {
	private static final long serialVersionUID = 1L;
	public static final DateTimeFormatter MT4_CSV_DATE_FORMAT = DateTimeFormatter
			.ofPattern("yyyy.MM.dd,HH:mm")
			.withZone(ZoneOffset.UTC);
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "time")
	private Instant time;
	private double low;
	private double high;
	private double open;
	private double close;
	private long volume;

	@Transient
	private Period period;

	@Transient
	private Calendar calendar;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Instant getTime() {
		return time;
	}
	public void setTime(Instant time) {
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

	public double getHeigh() {
		return high - low;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Period getPeriod() {
		return period;
	}

	public String toMt4CsvLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(MT4_CSV_DATE_FORMAT.format(time))
				.append(",").append(open)
				.append(",").append(high)
				.append(",").append(low)
				.append(",").append(close)
				.append(",").append(volume)
				.append("\n");
		return sb.toString();
	}

}
