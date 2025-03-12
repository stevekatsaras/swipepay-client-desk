package io.swipepay.clientdesk.exception;

public class ClientTransactionSearchException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientTransactionSearchException(String message, Throwable cause) {
		super(message, cause);
	}
}