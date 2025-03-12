package io.swipepay.clientdesk.domain.enums;

public enum CardBrand {
	Visa("Visa"),
	MasterCard("MasterCard"),
	AmericanExpress("American Express");
	
	private final String brand;
	
	private CardBrand(String brand) {
		this.brand = brand;
	}

	public String getBrand() {
		return brand;
	}
}