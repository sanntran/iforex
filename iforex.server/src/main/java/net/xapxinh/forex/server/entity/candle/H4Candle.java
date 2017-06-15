package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.wave.H4Wave;

public class H4Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.H4.getValue();
	}
	
	@Override
	public Class<? extends Wave> getWaveClass() {
		return H4Wave.class;
	}

	@Override
	public Wave newWave() {
		return new H4Wave();
	}
}
