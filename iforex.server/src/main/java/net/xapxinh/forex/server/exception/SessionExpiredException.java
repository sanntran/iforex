package net.xapxinh.forex.server.exception;

public class SessionExpiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public SessionExpiredException(String sessionId) {
		super("SessionExpired: " + sessionId);
	}
}
