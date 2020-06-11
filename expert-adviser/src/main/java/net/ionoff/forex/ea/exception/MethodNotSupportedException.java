package net.ionoff.forex.ea.exception;

public class MethodNotSupportedException extends RuntimeException {

    public MethodNotSupportedException(String method) {
        super(String.format("Method '%s' is not supported", method));
    }
}
