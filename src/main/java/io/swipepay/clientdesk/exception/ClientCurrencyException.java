package io.swipepay.clientdesk.exception;

public class ClientCurrencyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientCurrencyException(String message, Throwable cause) {
		super(message, cause);
	}
}