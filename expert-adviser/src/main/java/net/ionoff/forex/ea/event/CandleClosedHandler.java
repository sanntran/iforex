package net.ionoff.forex.ea.event;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.service.AverageService;
import net.ionoff.forex.ea.service.SupportService;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

@Component
@AllArgsConstructor
public class CandleClosedHandler implements Observer {

	private AverageService averageService;
	private SupportService supportService;

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof CandleClosedEvent)) {
			return;
		}
		Candle candle = ((CandleClosedEvent) arg).getCandle();
		onCandleClosed(candle);
	}

	private void onCandleClosed(Candle candle) {
		averageService.createAverage(candle);
	}
}
