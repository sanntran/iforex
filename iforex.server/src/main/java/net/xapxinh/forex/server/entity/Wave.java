package net.xapxinh.forex.server.entity;

import java.util.List;

public abstract class Wave extends Pojo {
	
	private static final long serialVersionUID = 1L;
	
	private List<WaveCandle> waveCandles;

	public List<WaveCandle> getWaveCandles() {
		return waveCandles;
	}

	public void setWaveCandles(List<WaveCandle> waveCandles) {
		this.waveCandles = waveCandles;
	}
}
