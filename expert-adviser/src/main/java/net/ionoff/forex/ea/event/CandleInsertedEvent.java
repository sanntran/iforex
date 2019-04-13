package net.ionoff.forex.ea.event;

import net.ionoff.forex.ea.model.Candle;

public class CandleInsertedEvent extends CandleEvent {

	public CandleInsertedEvent(Candle candle) {
		super(candle);
	}
}
