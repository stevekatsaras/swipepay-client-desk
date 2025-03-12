package io.swipepay.clientdesk.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class ClientUser {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "email_address", nullable = false, unique = true)
	private String emailAddress;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "telephone")
	private String telephone;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	
	@Column(name = "expired", nullable = false)
	private Boolean expired;
	
	@Column(name = "locked", nullable = false)
	private Boolean locked;
	
	@Column(name = "password_expired", nullable = false)
	private Boolean passwordExpired;
	
	@Column(name = "role", nullable = false)
	private String role;
	
	@Column(name = "modified", nullable = false)
	private LocalDateTime modified;
	
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	
	protected ClientUser() {
		
	}

	public ClientUser(
			String emailAddress, 
			String firstname, 
			String lastname, 
			String telephone, 
			String mobile, 
			String password, 
			Boolean enabled,
			Boolean expired, 
			Boolean locked, 
			Boolean passwordExpired,
			String role, 
			LocalDateTime modified, 
			Client client) {
		this.emailAddress = emailAddress;
		this.firstname = firstname;
		this.lastname = lastname;
		this.telephone = telephone;
		this.mobile = mobile;
		this.password = password;
		this.enabled = enabled;
		this.expired = expired;
		this.locked = locked;
		this.passwordExpired = passwordExpired;
		this.role = role;
		this.modified = modified;
		this.client = client;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(Boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}