package net.xapxinh.forex.server.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Wave;
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
			addCandleToLastWave(candle);
		}
	}

	private void addCandleToLastWave(Candle candle) {
		
		Wave wave = waveService.findLast(candle.getWaveClass());	
		
		if (wave == null) {
			wave = candle.newWave();
			waveService.insert(wave);
		}
		
		wave.addCandle(candle);
		candle.addWave(wave);
		waveService.update(wave);
		candleService.update(candle);
		
		separateWave(wave);
	}

	private void separateWave(Wave wave) {
		if (wave.getCandles() == null || wave.getCandles().size() < Wave.MIN_SIZE * 2) {
			return;
		}
		if (!wave.hasDirection()) {
			return;
		}
		
		List<Candle> candles = new ArrayList<>();
		
		//
		if (wave.isUp()) {
			// find the highest candle
			Candle highestPriceCandle = wave.getFirstCandle();
			int highestPriceCandleIdx = 0;
			for (int i = 1; i < candles.size(); i++) {
				if (highestPriceCandle.getHigh() < wave.getCandles().get(i).getHigh()) {
					highestPriceCandle = candles.get(i);
					highestPriceCandleIdx = i;
				}
			}
			if ((candles.size() - highestPriceCandleIdx) < Wave.MIN_SIZE) {
				return;
			}			
			if (wave.getLastCandle().getLow() >= highestPriceCandle.getLow()) {
				return;
			}
			if ((highestPriceCandle.getHigh() - wave.getLastCandle().getLow()) <= wave.getMinHeigh()) {
				return;
			}			
			for (int i = highestPriceCandleIdx; i < wave.getCandles().size(); i++) {
				candles.add(wave.getCandles().get(i));
			}
			for (int i = highestPriceCandleIdx + 1; i < wave.getCandles().size();) {
				Candle candle = wave.getCandles().get(i);
				candle.removeWave(wave);
				wave.getCandles().remove(i);
				candleService.update(candle);
				waveService.update(wave);
			}
			
			Wave newWave = wave.newInstance();
			newWave.setCandles(candles);
			waveService.insert(newWave);
			for (Candle candle : candles) {
				candle.addWave(newWave);
				candleService.update(candle);
			}
		}
		else if (wave.isDown()) {
			// find the lowest candle
			Candle lowestPriceCandle = wave.getFirstCandle();
			int lowestPriceCandleIdx = 0;
			for (int i = 1; i < candles.size(); i++) {
				if (lowestPriceCandle.getLow() > wave.getCandles().get(i).getLow()) {
					lowestPriceCandle = candles.get(i);
					lowestPriceCandleIdx = i;
				}
			}
			if ((candles.size() - lowestPriceCandleIdx) < Wave.MIN_SIZE) {
				return;
			}			
			if (wave.getLastCandle().getHigh() <= lowestPriceCandle.getHigh()) {
				return;
			}
			if ((wave.getLastCandle().getHigh() - lowestPriceCandle.getLow()) <= wave.getMinHeigh()) {
				return;
			}
			for (int i = lowestPriceCandleIdx; i < wave.getCandles().size(); i++) {
				candles.add(wave.getCandles().get(i));
			}
			for (int i = lowestPriceCandleIdx + 1; i < wave.getCandles().size();) {
				Candle candle = wave.getCandles().get(i);
				candle.removeWave(wave);
				wave.getCandles().remove(i);
				candleService.update(candle);
				waveService.update(wave);
			}
			
			Wave newWave = wave.newInstance();
			newWave.setCandles(candles);
			waveService.insert(newWave);
			for (Candle candle : candles) {
				candle.addWave(newWave);
				candleService.update(candle);
			}
		}
		else {
			// does nothing
		}
	}	
}
