package io.swipepay.clientdesk.exception;

public class ClientCustomerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientCustomerException(String message, Throwable cause) {
		super(message, cause);
	}
}