package io.swipepay.clientdesk.exception;

public class ClientKeyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientKeyException(String message, Throwable cause) {
		super(message, cause);
	}
}