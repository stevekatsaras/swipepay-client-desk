package io.swipepay.clientdesk.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Client {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@OneToOne
	@JoinColumn(name = "business_entity_id", nullable = false)
	private BusinessEntity businessEntity;
	
	@Column(name = "business_entity_number")
	private String businessEntityNumber;
	
	@Column(name = "website")
	private String website;
	
	@Column(name = "address1")
	private String address1;
	
	@Column(name = "address2")
	private String address2;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "postcode")
	private String postcode;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "private_key", nullable = false)
	private String privateKey;
	
	@Column(name = "signup_date", nullable = false)
	private LocalDate signupDate;
	
	@Column(name = "cancel_date")
	private LocalDate cancelDate;
	
	@Column(name = "modified", nullable = false)
	private LocalDateTime modified;
	
	@OneToOne
	@JoinColumn(name = "country_id", nullable = false)
	private Country country;

	@OneToOne
	@JoinColumn(name = "plan_id", nullable = false)
	private Plan plan;
	
	protected Client() {
		
	}

	public Client(
			String name, 
			BusinessEntity businessEntity, 
			String businessEntityNumber, 
			String website, 
			String address1,
			String address2, 
			String city, 
			String state, 
			String postcode, 
			String status, 
			String privateKey, 
			LocalDate signupDate, 
			LocalDateTime modified, 
			Country country, 
			Plan plan) {
		this.name = name;
		this.businessEntity = businessEntity;
		this.businessEntityNumber = businessEntityNumber;
		this.website = website;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.postcode = postcode;
		this.status = status;
		this.privateKey = privateKey;
		this.signupDate = signupDate;
		this.modified = modified;
		this.country = country;
		this.plan = plan;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessEntity getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(BusinessEntity businessEntity) {
		this.businessEntity = businessEntity;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public LocalDate getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(LocalDate signupDate) {
		this.signupDate = signupDate;
	}

	public LocalDate getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}
	
	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}