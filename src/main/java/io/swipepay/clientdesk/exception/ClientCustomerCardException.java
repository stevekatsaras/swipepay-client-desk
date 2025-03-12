package io.swipepay.clientdesk.exception;

public class ClientCustomerCardException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientCustomerCardException(String message, Throwable cause) {
		super(message, cause);
	}
}