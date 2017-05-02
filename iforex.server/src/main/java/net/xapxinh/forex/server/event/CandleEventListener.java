package net.xapxinh.forex.server.event;

import java.util.Observable;
import java.util.Observer;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.persistence.service.IWaveService;

public class CandleEventListener implements Observer {

	private IWaveService waveService;
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof CandleInsertedEvent) {
			Candle candle = ((CandleInsertedEvent) arg).getCandle();
		}
	}
}
