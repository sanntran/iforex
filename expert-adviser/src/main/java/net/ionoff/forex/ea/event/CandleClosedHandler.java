package net.ionoff.forex.ea.event;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.service.AverageService;
import net.ionoff.forex.ea.service.ExtremaService;
import net.ionoff.forex.ea.service.PredictionService;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CandleClosedHandler implements Observer {

	private AverageService averageService;
	private ExtremaService extremaService;
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
		Optional<Average> average = averageService.createAverage(candle);
		average.ifPresent(avg -> extremaService.createSupportAndResistance(avg));
		average.ifPresent(avg -> predictionService.createPrediction(avg));
	}
}
