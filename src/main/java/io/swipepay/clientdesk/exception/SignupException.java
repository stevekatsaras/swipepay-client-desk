package io.swipepay.clientdesk.exception;

public class SignupException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SignupException(String message, Throwable cause) {
		super(message, cause);
	}
}