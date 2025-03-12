package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;

public class ClientTransactionPaymentForm {
	@NotBlank(message = "Profile is required")
	private String clientProfileId;
	
	@NotBlank(message = "Transaction type is required")
	private String transactionTypeId;	
	
	@NotBlank(message = "Description is required")
	private String description;
	
	@NotBlank(message = "Amount is required")
	private String amount;
	
	@NotBlank(message = "Currency is required")
	private String clientCurrencyId;
	
	@NotBlank(message = "Card number is required")
	@CreditCardNumber(message = "Card number is invalid")
	@ToStringExclude
	private String cardNumber;
	
	@NotBlank(message = "Expiry month is required")
	private String expiryMonth;
	
	@NotBlank(message = "Expiry year is required")
	private String expiryYear;
	
	@NotBlank(message = "CVV is required")
	@ToStringExclude
	private String cvv;
	
	public String getClientProfileId() {
		return clientProfileId;
	}
	
	public void setClientProfileId(String clientProfileId) {
		this.clientProfileId = clientProfileId;
	}
	
	public String getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(String transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getClientCurrencyId() {
		return clientCurrencyId;
	}

	public void setClientCurrencyId(String clientCurrencyId) {
		this.clientCurrencyId = clientCurrencyId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}