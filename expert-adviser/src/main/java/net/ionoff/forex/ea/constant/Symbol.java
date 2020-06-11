package net.ionoff.forex.ea.constant;

import net.ionoff.forex.ea.exception.SymbolNotSupportedException;

public enum Symbol {
	EURUSD;
	
	public boolean equals(String symbol) {
		return toString().equals(symbol);
	}

	public static void assertValid(String symbol) {
		if (!EURUSD.equals(symbol)) {
			throw new SymbolNotSupportedException(symbol);
		}
	}
}
