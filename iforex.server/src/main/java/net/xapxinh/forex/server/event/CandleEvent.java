package net.xapxinh.forex.server.event;

import net.xapxinh.forex.server.entity.Candle;

public class CandleEvent {
	
	private final Candle candle;
	
	public CandleEvent(Candle candle) {
		this.candle = candle; 
	}

	public Candle getCandle() {
		return candle;
	}
}
