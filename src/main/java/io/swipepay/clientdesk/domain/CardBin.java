package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"bin", "country_isoa2_code", "country_isoa3_code"}))
public class CardBin {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "bin", nullable = false, unique = true)
	private String bin;
	
	@Column(name = "card_brand")
	private String cardBrand;
	
	@Column(name = "bank")
	private String bank;
	
	@Column(name = "card_type")
	private String cardType;
	
	@Column(name = "card_category")
	private String cardCategory;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "country_isoa2_code")
	private String countryIsoa2Code;
	
	@Column(name = "country_isoa3_code")
	private String countryIsoa3Code;
	
	@Column(name = "country_iso_number")
	private String countryIsoNumber;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "bank_phone")
	private String bankPhone;
	
	protected CardBin() {
		
	}
	
	public CardBin(
			String bin, 
			String cardBrand, 
			String bank, 
			String cardType, 
			String cardCategory, 
			String country,
			String countryIsoa2Code, 
			String countryIsoa3Code, 
			String countryIsoNumber, 
			String location,
			String bankPhone) {
		this.bin = bin;
		this.cardBrand = cardBrand;
		this.bank = bank;
		this.cardType = cardType;
		this.cardCategory = cardCategory;
		this.country = country;
		this.countryIsoa2Code = countryIsoa2Code;
		this.countryIsoa3Code = countryIsoa3Code;
		this.countryIsoNumber = countryIsoNumber;
		this.location = location;
		this.bankPhone = bankPhone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardCategory() {
		return cardCategory;
	}

	public void setCardCategory(String cardCategory) {
		this.cardCategory = cardCategory;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryIsoa2Code() {
		return countryIsoa2Code;
	}

	public void setCountryIsoa2Code(String countryIsoa2Code) {
		this.countryIsoa2Code = countryIsoa2Code;
	}

	public String getCountryIsoa3Code() {
		return countryIsoa3Code;
	}

	public void setCountryIsoa3Code(String countryIsoa3Code) {
		this.countryIsoa3Code = countryIsoa3Code;
	}

	public String getCountryIsoNumber() {
		return countryIsoNumber;
	}

	public void setCountryIsoNumber(String countryIsoNumber) {
		this.countryIsoNumber = countryIsoNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBankPhone() {
		return bankPhone;
	}

	public void setBankPhone(String bankPhone) {
		this.bankPhone = bankPhone;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}