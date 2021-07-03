package net.ionoff.forex.ea.event;

import net.ionoff.forex.ea.model.Candle;

public class CandleClosedEvent extends CandleEvent {

	public CandleClosedEvent(Candle candle) {
		super(candle);
	}
}
