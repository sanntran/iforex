package net.xapxinh.forex.server.event;

import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.candle.EurUsdM5Candle;
import net.xapxinh.forex.server.entity.wave.EurUsdM5Wave;
import net.xapxinh.forex.server.persistence.service.ICandleService;
import net.xapxinh.forex.server.persistence.service.IWaveService;

public class CandleEventListener implements Observer {

	@Autowired
	private IWaveService waveService;
	
	@Autowired
	private ICandleService candleService;
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof CandleInsertedEvent) {
			Candle candle = ((CandleInsertedEvent) arg).getCandle();
			if (candle instanceof EurUsdM5Candle) {
				addCandleToLastM5Wave(candle);
			}
		}
	}

	private void addCandleToLastM5Wave(Candle candle) {
		EurUsdM5Candle m5Candle = (EurUsdM5Candle)candle;
		EurUsdM5Wave m5Wave = waveService.findLast(EurUsdM5Wave.class);	
		
		if (m5Wave == null) {
			m5Wave = new EurUsdM5Wave();
			waveService.insert(m5Wave);
		}
		
		m5Wave.addCandle(m5Candle);
		m5Candle.addWave(m5Wave);
		waveService.update(m5Wave);
		candleService.update(m5Candle);
		
		separateWave(m5Wave);
	}

	private void separateWave(Wave wave) {
		if (wave.getCandles() == null || wave.getCandles().size() < 8) {
			return;
		}
		
	}
}
