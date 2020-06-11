package net.ionoff.forex.ea.event;

import net.ionoff.forex.ea.entity.Candle;

public class CandleEvent {
	
	private final Candle candle;
	
	public CandleEvent(Candle candle) {
		this.candle = candle; 
	}

	public Candle getCandle() {
		return candle;
	}
}
