package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;

public class ClientCustomerCardForm {
	public interface ValidationAdd {}
	public interface ValidationEdit {}
	
	private String code;
	
	@NotBlank(groups = {ValidationAdd.class, ValidationEdit.class}, message = "Cardholder name is required")
	private String cardHolderName;
	
	private String cardHolderEmail;
	
	@NotBlank(groups = {ValidationAdd.class}, message = "Card number is required")
	@CreditCardNumber(groups = {ValidationAdd.class}, message = "Card number is invalid")
	@ToStringExclude
	private String cardNumber;
	
	private String cardPan;
	
	@NotBlank(groups = {ValidationAdd.class, ValidationEdit.class}, message = "Expiry month is required")
	private String expiryMonth;
	
	@NotBlank(groups = {ValidationAdd.class, ValidationEdit.class}, message = "Expiry year is required")
	private String expiryYear;
	
	private Boolean isExpired;
	
	private Boolean enabled;
	private Boolean isDefault;
	private String modified;
	
	private String cardBrand;
	private String cardBank;
	private String cardCountry;
	private String cardLocation;
	private String cardBankPhone;
	private String cardIcon;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardHolderEmail() {
		return cardHolderEmail;
	}

	public void setCardHolderEmail(String cardHolderEmail) {
		this.cardHolderEmail = cardHolderEmail;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardPan() {
		return cardPan;
	}

	public void setCardPan(String cardPan) {
		this.cardPan = cardPan;
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

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getCardBank() {
		return cardBank;
	}

	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}

	public String getCardCountry() {
		return cardCountry;
	}

	public void setCardCountry(String cardCountry) {
		this.cardCountry = cardCountry;
	}

	public String getCardLocation() {
		return cardLocation;
	}

	public void setCardLocation(String cardLocation) {
		this.cardLocation = cardLocation;
	}

	public String getCardBankPhone() {
		return cardBankPhone;
	}

	public void setCardBankPhone(String cardBankPhone) {
		this.cardBankPhone = cardBankPhone;
	}

	public String getCardIcon() {
		return cardIcon;
	}

	public void setCardIcon(String cardIcon) {
		this.cardIcon = cardIcon;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}