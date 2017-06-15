package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Candle.PERIOD;
import net.xapxinh.forex.server.entity.Wave;

public class D1Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new D1Wave();
	}
	
	@Override
	public int getPeriod() {
		return PERIOD.D1.getValue();
	}

	@Override
	public double getMinHeigh() {
		return 0;
	}
	
	public Class<D1Wave> getWaveClass() {
		return D1Wave.class;
	}
}
