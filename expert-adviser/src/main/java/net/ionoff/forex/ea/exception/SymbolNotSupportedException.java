package net.ionoff.forex.ea.exception;

public class SymbolNotSupportedException extends RuntimeException {

    public SymbolNotSupportedException(String symbol) {
        super(String.format("Symbol '%s' is not supported", symbol));
    }
}
