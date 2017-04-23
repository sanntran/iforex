package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;

public class EurUsdM5Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	public int getPeriod() {
		return PERIOD.M5.getValue();
	}
}
