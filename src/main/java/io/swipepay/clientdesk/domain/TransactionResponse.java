package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class TransactionResponse {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "code", nullable = false, unique = true)
	private String code;
	
	@Column(name = "text", nullable = false)
	private String text;
	
	@Column(name = "approved", nullable = false)
	private Boolean approved;
	
	protected TransactionResponse() {
		
	}
	
	public TransactionResponse(String code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}