package net.xapxinh.forex.server.entity;

public enum Symbol {
	EURUSD;
	
	public boolean equals(String symbol) {
		return toString().equals(symbol);
	}
	
}
