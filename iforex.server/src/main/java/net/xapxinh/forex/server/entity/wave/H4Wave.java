package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.Candle.PERIOD;

public class H4Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new H4Wave();
	}
	
	public int getPeriod() {
		return PERIOD.H4.getValue();
	}
	

	public double getMinHeigh() {
		return 0;
	}
}
