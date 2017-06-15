package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.Candle.PERIOD;

public class M30Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new M30Wave();
	}
	
	public int getPeriod() {
		return PERIOD.M30.getValue();
	}
	

	public double getMinHeigh() {
		return 0;
	}
}