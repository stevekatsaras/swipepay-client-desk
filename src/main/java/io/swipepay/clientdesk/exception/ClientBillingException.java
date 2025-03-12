package io.swipepay.clientdesk.exception;

public class ClientBillingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientBillingException(String message, Throwable cause) {
		super(message, cause);
	}
}