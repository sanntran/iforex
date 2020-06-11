package net.ionoff.forex.ea.event;

import net.ionoff.forex.ea.entity.Candle;

import java.util.Observable;
import java.util.Observer;

public class CandleEventListener implements Observer {


	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof CandleInsertedEvent) {
			Candle candle = ((CandleInsertedEvent) arg).getCandle();
		}
	}

}
