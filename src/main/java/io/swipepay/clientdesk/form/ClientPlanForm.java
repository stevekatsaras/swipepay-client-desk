package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClientPlanForm {
	private String cost;
	private String unit;
	private String txnCap;
	private String txnCapExcessCost;
	
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTxnCap() {
		return txnCap;
	}

	public void setTxnCap(String txnCap) {
		this.txnCap = txnCap;
	}

	public String getTxnCapExcessCost() {
		return txnCapExcessCost;
	}

	public void setTxnCapExcessCost(String txnCapExcessCost) {
		this.txnCapExcessCost = txnCapExcessCost;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}