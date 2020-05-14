package net.ionoff.forex.ea.model.candle;

import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.constant.Period;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "eur_usd_v300_candles")
public class V300Candle extends Candle {

	private static final long serialVersionUID = 1L;

	public V300Candle() {
		setPeriod(Period.M5);
	}
}
