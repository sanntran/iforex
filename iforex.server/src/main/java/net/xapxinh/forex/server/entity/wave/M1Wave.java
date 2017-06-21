package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Candle.PERIOD;
import net.xapxinh.forex.server.entity.Wave;

public class M1Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new M1Wave();
	}
	
	@Override
	public int getPeriod() {
		return PERIOD.M1.getValue();
	}	

	@Override
	public double getMinHeigh() {
		return 27;
	}
}
