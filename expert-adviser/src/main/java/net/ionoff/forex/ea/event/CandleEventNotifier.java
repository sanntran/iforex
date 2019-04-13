package net.ionoff.forex.ea.event;

import java.util.Observable;
import java.util.Observer;

public class CandleEventNotifier extends Observable {

	public CandleEventNotifier(Observer ...observers ) {
		for (Observer observer : observers) {
			addObserver(observer);
		}
	}
	
	public void notifyListeners(CandleEvent event) {
		setChanged();
		notifyObservers(event);
	}
}
