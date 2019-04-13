package net.ionoff.forex.ea.model.candle;

import net.ionoff.forex.ea.model.Candle;

public abstract class M1Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.M1.getValue();
	}

}
