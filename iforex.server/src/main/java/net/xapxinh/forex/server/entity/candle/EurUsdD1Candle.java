package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;

public class EurUsdD1Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	public int getPeriod() {
		return PERIOD.D1.getValue();
	}
}
