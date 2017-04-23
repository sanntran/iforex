package net.xapxinh.forex.server.entity;

public abstract class WaveCandle extends Pojo {
	
	private static final long serialVersionUID = 1L;
	
	private Wave wave;
	private Candle candle;

	public Wave getWave() {
		return wave;
	}
	public void setWave(Wave wave) {
		this.wave = wave;
	}
	
	public Candle getCandle() {
		return candle;
	}
	public void setCandle(Candle candle) {
		this.candle = candle;
	}
}
