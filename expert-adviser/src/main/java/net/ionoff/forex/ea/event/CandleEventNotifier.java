package net.ionoff.forex.ea.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Component
public class CandleEventNotifier extends Observable {

	@Autowired
	public CandleEventNotifier(List<Observer> observers) {
		for (Observer observer : observers) {
			addObserver(observer);
		}
	}
	
	public void fireCandleEvent(CandleEvent event) {
		setChanged();
		notifyObservers(event);
	}
}
