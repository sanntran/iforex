package net.ionoff.forex.ea.model;

public enum Symbol {
	EURUSD;
	
	public boolean equals(String symbol) {
		return toString().equals(symbol);
	}
	
}
