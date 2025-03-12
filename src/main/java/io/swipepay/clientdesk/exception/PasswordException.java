package io.swipepay.clientdesk.exception;

public class PasswordException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PasswordException(String message, Throwable cause) {
		super(message, cause);
	}
}