package net.xapxinh.forex.server.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.entity.candle.EurUsdM5Candle;
import net.xapxinh.forex.server.entity.wave.EurUsdM5Wave;
import net.xapxinh.forex.server.persistence.service.ICandleService;
import net.xapxinh.forex.server.persistence.service.IWaveService;
import net.xapxinh.forex.server.statistic.EurUsdStatistic;

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
				//addCandleToLastM5Wave(candle);
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
		if (wave.getCandles() == null || wave.getCandles().size() < Wave.MIN_SIZE * 2) {
			return;
		}
		if (wave.isEnd()) { // 
			return;
		}
		
		List<Candle> candles = new ArrayList<>();
		
		
		for (int i = Wave.MIN_SIZE; i > 0; i--) {
			candles.add(wave.getCandles().get(wave.getCandles().size() - i));
		}
		if (isNewWave(candles, wave)) {
			Wave newWave = wave.newInstance();
		}
	}

	private boolean isNewWave(List<Candle> candles, Wave wave) {
		if (wave.isDown()) {
			
		}
		
		
		int size = candles.size();
		for (int i = 1; i < size; i++) {
			///////////////
		}
		return false;
	}
	
	private double getMinM5WaveHeigh() {
		return (EurUsdStatistic.getEuUsSessionEurUsdM15AvgHeigh() / 2) * 0.9;
	}
}
