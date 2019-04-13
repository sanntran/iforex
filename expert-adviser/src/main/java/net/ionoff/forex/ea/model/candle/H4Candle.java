package net.ionoff.forex.ea.model.candle;

import net.ionoff.forex.ea.model.Candle;

public abstract class H4Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.H4.getValue();
	}
}
