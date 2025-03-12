package io.swipepay.clientdesk.domain.enums;

public enum DateTimePattern {
	dMMMyyyy("d MMM yyyy"),
	dMMMyyyyhhmma("d MMM yyyy hh:mm a");
	
	private final String pattern;
	
	private DateTimePattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String toString() {
		return pattern;
	}
}