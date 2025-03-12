package io.swipepay.clientdesk.exception;

public class ClientCardException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ClientCardException(String message, Throwable cause) {
		super(message, cause);
	}
}