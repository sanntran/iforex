package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.Candle.PERIOD;

public class M15Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new M15Wave();
	}
	
	public int getPeriod() {
		return PERIOD.M15.getValue();
	}
	

	public double getMinHeigh() {
		return 0;
	}
}
