package net.xapxinh.forex.server.entity;

import java.util.List;

public abstract class Wave extends Pojo {
	
	private static final long serialVersionUID = 1L;
	
	private List<Candle> candles;

	public List<Candle> getCandles() {
		return candles;
	}

	public void setCandles(List<Candle> candles) {
		this.candles = candles;
	}
}
