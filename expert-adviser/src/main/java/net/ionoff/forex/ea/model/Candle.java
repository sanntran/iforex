package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ionoff.forex.ea.constant.Period;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "candles")
public class Candle implements Entity {
	private static final long serialVersionUID = 1L;
	public static final DateTimeFormatter MT4_CSV_DATE_FORMAT = DateTimeFormatter
			.ofPattern("yyyy.MM.dd,HH:mm")
			.withZone(ZoneOffset.UTC);
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Instant time;
	private Instant instant;
	private double low;
	private double high;
	private double open;
	private double close;
	private int volume;
	private double pivot;

	@Transient
	private Period period;
	
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
		return Math.abs(close - open);
	}

	public double getHeight() {
		return new BigDecimal((high - low) * 100000).intValue();
	}

	@Transient
	public Instant getCloseTime() {
		return time.plusSeconds(60); // period 1 minute
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Candle candle = (Candle) o;
		return Objects.equals(time, candle.time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time);
	}
}
