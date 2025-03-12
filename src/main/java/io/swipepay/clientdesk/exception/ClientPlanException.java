package io.swipepay.clientdesk.exception;

public class ClientPlanException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClientPlanException(String message, Throwable cause) {
		super(message, cause);
	}
}