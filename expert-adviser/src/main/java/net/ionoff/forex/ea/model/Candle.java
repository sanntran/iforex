package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "candles")
public class Candle implements Entity {
	private static final long serialVersionUID = 1L;

	@Getter
	public enum Period {
		SHORT("CANDLE", 1, Duration.ofMinutes(5)),
		MEDIUM("CANDLE", 6, Duration.ofMinutes(30)),
		LONG("CANDLE", 18, Duration.ofMinutes(90));
		private final String type;
		private final int size;
		private final Duration duration;
		Period(String type, int size, Duration duration) {
			this.type = type;
			this.size = size;
			this.duration = duration;
		}

		public boolean isMinuteBased() {
			return "MINUTE".equals(type);
		}

		public boolean isVolumeBased() {
			return "VOLUME".equals(type);
		}

		public boolean isHeightBased() {
			return "HEIGHT".equals(type);
		}

		public boolean isCandleBased() {
			return "CANDLE".equals(type);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Enumerated(value = EnumType.STRING)
	private Period period;
	private Instant time;
	private double low;
	private double high;
	private double open;
	private double close;
	private int volume;
	private double pivot;
	private Integer size;
	private Instant opened;
	private Instant closed;

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

	public int getHeight() {
		return new BigDecimal((high - low) * 100000).intValue();
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


	@Transient
	public boolean isClosed() {
		return size != null && size >= period.getSize();
	}
}
