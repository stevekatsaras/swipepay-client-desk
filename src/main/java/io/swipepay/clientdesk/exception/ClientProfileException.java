package io.swipepay.clientdesk.exception;

public class ClientProfileException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientProfileException(String message, Throwable cause) {
		super(message, cause);
	}
}