package io.swipepay.clientdesk.exception;

public class ClientUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientUserException(String message, Throwable cause) {
		super(message, cause);
	}
}