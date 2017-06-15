package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.wave.H1Wave;

public class H1Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.H1.getValue();
	}
	
	@Override
	public Class<? extends Wave> getWaveClass() {
		return H1Wave.class;
	}

	@Override
	public Wave newWave() {
		return new H1Wave();
	}
}
