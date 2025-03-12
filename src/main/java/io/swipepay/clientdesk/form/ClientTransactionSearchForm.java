package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClientTransactionSearchForm {
	private String transactionCode;
	private String clientProfileIds;
	private String transactionTypeIds;
	private String description;
	private String amountCriteria;
	private String amount;
	private String clientCurrencyIds;	
	
	private String cardBin;
	private String expiryMonth;
	private String expiryYear;
	
//	private String event;
//	private String clientCardId;
//	private String status;
//	private String transactionResponseId;
	
	
	



	public String getTransactionCode() {
		return transactionCode;
	}
	
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	
	public String getClientProfileIds() {
		return clientProfileIds;
	}
	
	public void setClientProfileIds(String clientProfileIds) {
		this.clientProfileIds = clientProfileIds;
	}
	
	public String getTransactionTypeIds() {
		return transactionTypeIds;
	}

	public void setTransactionTypeIds(String transactionTypeIds) {
		this.transactionTypeIds = transactionTypeIds;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmountCriteria() {
		return amountCriteria;
	}

	public void setAmountCriteria(String amountCriteria) {
		this.amountCriteria = amountCriteria;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getClientCurrencyIds() {
		return clientCurrencyIds;
	}

	public void setClientCurrencyIds(String clientCurrencyIds) {
		this.clientCurrencyIds = clientCurrencyIds;
	}

	public String getCardBin() {
		return cardBin;
	}

	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}