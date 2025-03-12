package io.swipepay.clientdesk.form;

import javax.validation.constraints.AssertTrue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class SignupForm {
	public interface ValidationUser {}
	public interface ValidationClient {}
	public interface ValidationPlan {}
	public interface ValidationPayment {}
	
	@NotBlank(groups = ValidationUser.class, message = "Email address is required")
	@Email(groups = ValidationUser.class, message = "Email address is invalid")
	private String emailAddress;
	
	@NotBlank(groups = ValidationUser.class, message = "First name is required")
	private String firstname;
	
	@NotBlank(groups = ValidationUser.class, message = "Last name is required")
	private String lastname;
	
	private String telephone;
	private String mobile;
	
	@NotBlank(groups = ValidationUser.class, message = "Password is required")
	private String password;
	
	@NotBlank(groups = ValidationUser.class, message = "Confirm password is required")
	private String confirmPassword;
	
	@NotBlank(groups = ValidationClient.class, message = "Client name is required")
	private String clientName;
	
	@NotBlank(groups = ValidationClient.class, message = "Business entity is required")
	private String businessEntityId;
	
	private String businessEntityNumber;
	private String website;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String postcode;
	
	@NotBlank(groups = ValidationClient.class, message = "Country is required")
	private String countryId;
	
	@NotBlank(groups = ValidationPlan.class, message = "Plan is required")
	private String planId;
	
	@NotBlank(groups = ValidationPayment.class, message = "Cardholder name is required")
	private String cardHolderName;
	
	private String cardHolderEmail;
	
	@NotBlank(groups = ValidationPayment.class, message = "Card number is required")
	@CreditCardNumber(groups = ValidationPayment.class, message = "Card number is invalid")
	@ToStringExclude
	private String cardNumber;
	
	@NotBlank(groups = ValidationPayment.class, message = "Expiry month is required")
	private String expiryMonth;
	
	@NotBlank(groups = ValidationPayment.class, message = "Expiry year is required")
	private String expiryYear;
	
	@NotBlank(groups = ValidationPayment.class, message = "CVV is required")
	@ToStringExclude
	private String cvv;
	
	@AssertTrue(groups = ValidationPayment.class, message = "Agreement must be checked")
	private Boolean iAgree;
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getBusinessEntityId() {
		return businessEntityId;
	}

	public void setBusinessEntityId(String businessEntityId) {
		this.businessEntityId = businessEntityId;
	}

	public String getBusinessEntityNumber() {
		return businessEntityNumber;
	}

	public void setBusinessEntityNumber(String businessEntityNumber) {
		this.businessEntityNumber = businessEntityNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
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

	public Boolean getiAgree() {
		return iAgree;
	}

	public void setiAgree(Boolean iAgree) {
		this.iAgree = iAgree;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}