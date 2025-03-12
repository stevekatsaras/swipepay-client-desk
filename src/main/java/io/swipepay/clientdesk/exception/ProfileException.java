package io.swipepay.clientdesk.exception;

public class ProfileException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProfileException(String message, Throwable cause) {
		super(message, cause);
	}
}