package io.swipepay.clientdesk.exception;

public class ClientTransactionPaymentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ClientTransactionPaymentException(String message, Throwable throwable) {
		super(message, throwable);
	}
}