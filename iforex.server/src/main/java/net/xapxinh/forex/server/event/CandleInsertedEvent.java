package net.xapxinh.forex.server.event;

import net.xapxinh.forex.server.entity.Candle;

public class CandleInsertedEvent extends CandleEvent {

	public CandleInsertedEvent(Candle candle) {
		super(candle);
	}
}
