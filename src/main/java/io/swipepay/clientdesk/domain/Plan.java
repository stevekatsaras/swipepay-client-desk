package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Plan {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name = "cost", nullable = false)
	private Long cost;
	
	@Column(name = "cost_type", nullable = false)
	private String costType;
	
	@Column(name = "unit", nullable = false)
	private String unit;
	
	@Column(name = "txn_cap", nullable = false)
	private Long txnCap;

	@Column(name = "txn_cap_excess_cost", nullable = false)
	private Long txnCapExcessCost;
	
	@OneToOne
	@JoinColumn(name = "currency_id", nullable = false)
	private Currency currency;
	
	protected Plan() {
		
	}

	public Plan(
			String type, 
			Long cost, 
			String costType, 
			String unit, 
			Long txnCap,
			Long txnCapExcessCost, 
			Currency currency) {
		this.type = type;
		this.cost = cost;
		this.costType = costType;
		this.unit = unit;
		this.txnCap = txnCap;
		this.txnCapExcessCost = txnCapExcessCost;
		this.currency = currency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}
	
	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getTxnCap() {
		return txnCap;
	}

	public void setTxnCap(Long txnCap) {
		this.txnCap = txnCap;
	}

	public Long getTxnCapExcessCost() {
		return txnCapExcessCost;
	}

	public void setTxnCapExcessCost(Long txnCapExcessCost) {
		this.txnCapExcessCost = txnCapExcessCost;
	}
	
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}