package net.ionoff.forex.ea.event;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Prediction;
import net.ionoff.forex.ea.service.AverageService;
import net.ionoff.forex.ea.service.PredictionService;
import net.ionoff.forex.ea.service.ResistanceService;
import net.ionoff.forex.ea.service.SupportService;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CandleClosedHandler implements Observer {

	private AverageService averageService;
	private PredictionService predictionService;
	private SupportService supportService;
	private ResistanceService resistanceService;

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
		Optional<Prediction> prediction = average.isPresent() ? predictionService.createPrediction(average.get()) : Optional.empty();
		prediction.ifPresent(p -> {
			supportService.updateSupport(average.get(), p);
			resistanceService.updateResistance(average.get(), p);
		});
	}
}
