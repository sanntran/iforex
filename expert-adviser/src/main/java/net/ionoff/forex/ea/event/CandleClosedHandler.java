package net.ionoff.forex.ea.event;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.service.AverageService;
import net.ionoff.forex.ea.service.PredictionService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@AllArgsConstructor
public class CandleClosedHandler implements Observer {

	private CandleRepository candleRepository;
	private AverageService averageService;
	private PredictionService predictionService;

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof CandleClosedEvent)) {
			return;
		}
		Candle candle = ((CandleClosedEvent) arg).getCandle();
		onCandleClosed(candle);
	}

	private void onCandleClosed(Candle candle) {
		List<Candle> candles = candleRepository.findFromIdToId(candle.getId() - 12, candle.getId());
		if (candles.size() < 12) {
			return;
		}
		Optional<Average> lastAvg = averageService.findLatest();
		if (!lastAvg.isPresent()
				|| Duration.between(lastAvg.get().getTime(), candle.getTime())
					.compareTo(Duration.ofMinutes(60)) >= 0) {
			List<Average> averages = averageService.createAverage(candles);
			predictionService.createPrediction(averages);
		}
	}
}
