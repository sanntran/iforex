package net.ionoff.forex.ea.model.candle;

import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.constant.Period;

import javax.persistence.*;

@Entity
@Table(name = "eur_usd_m1_candles")
public class M1Candle extends Candle {

	private static final long serialVersionUID = 1L;

	@Override
	public Period getPeriod() {
		return Period.M1;
	}
}
