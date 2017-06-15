package net.xapxinh.forex.server.entity.candle;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.wave.M15Wave;

public class M15Candle extends Candle {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPeriod() {
		return PERIOD.M15.getValue();
	}
	
	@Override
	public Class<? extends Wave> getWaveClass() {
		return M15Wave.class;
	}
	
	@Override
	public Wave newWave() {
		return new M15Wave();
	}
}
