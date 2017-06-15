package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.wave.D1Wave;

public class D1Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.D1.getValue();
	}

	@Override
	public Class<? extends Wave> getWaveClass() {
		return D1Wave.class;
	}

	@Override
	public Wave newWave() {
		return new D1Wave();
	}
}
