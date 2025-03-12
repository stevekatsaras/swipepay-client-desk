package io.swipepay.clientdesk.exception;

public class ClientFraudprotectException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ClientFraudprotectException(String message, Throwable cause) {
		super(message, cause);
	}
}