package net.ionoff.forex.ea.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "candle_v300")
public class CandleV300 extends Candle {

	private static final long serialVersionUID = 1L;

	public CandleV300() {
		setPeriod(Period.M5);
	}
}
