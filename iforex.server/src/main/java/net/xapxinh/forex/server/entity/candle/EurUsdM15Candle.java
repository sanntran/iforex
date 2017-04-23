package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;

public class EurUsdM15Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	public int getPeriod() {
		return PERIOD.M15.getValue();
	}
}
