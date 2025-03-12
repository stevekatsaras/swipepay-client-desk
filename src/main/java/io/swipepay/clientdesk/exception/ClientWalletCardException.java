package io.swipepay.clientdesk.exception;

public class ClientWalletCardException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientWalletCardException(String message, Throwable cause) {
		super(message, cause);
	}
}