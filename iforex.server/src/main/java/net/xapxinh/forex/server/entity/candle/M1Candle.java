package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.wave.M1Wave;

public class M1Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.M1.getValue();
	}
	
	@Override
	public Class<? extends Wave> getWaveClass() {
		return M1Wave.class;
	}
	

	@Override
	public Wave newWave() {
		return new M1Wave();
	}
}
