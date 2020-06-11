package net.ionoff.forex.ea.entity;

import net.ionoff.forex.ea.constant.Period;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "v300_candles")
public class CandleV300 extends Candle {

	private static final long serialVersionUID = 1L;

	public CandleV300() {
		setPeriod(Period.M5);
	}
}
