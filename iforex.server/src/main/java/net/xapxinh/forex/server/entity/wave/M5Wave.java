package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.Candle.PERIOD;

public class M5Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new M5Wave();
	}
	
	public int getPeriod() {
		return PERIOD.M5.getValue();
	}	

	public double getMinHeigh() {
		return 34;
	}
}
