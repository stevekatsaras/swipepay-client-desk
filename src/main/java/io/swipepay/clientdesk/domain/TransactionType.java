package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class TransactionType {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "new_txn", nullable = false)
	private Boolean newTxn;
	
	protected TransactionType() {
		
	}
	
	public TransactionType(String name, Boolean newTxn) {
		this.name = name;
		this.newTxn = newTxn;
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

	public Boolean getNewTxn() {
		return newTxn;
	}

	public void setNewTxn(Boolean newTxn) {
		this.newTxn = newTxn;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}