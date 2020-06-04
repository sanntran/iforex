package net.ionoff.forex.ea.exception;

public class SymbolNotSupportedException extends RuntimeException {

    public SymbolNotSupportedException(String symbol) {
        super(String.format("Symbol 'symbol' is %s not supported", symbol));
    }
}
