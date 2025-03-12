package io.swipepay.clientdesk.exception;

public class ClientAccountException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientAccountException(String message, Throwable cause) {
		super(message, cause);
	}
}