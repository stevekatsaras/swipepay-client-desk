package io.swipepay.clientdesk.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"code", "client_id"})
})
public class ClientBilling {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "period_from", nullable = false)
	private LocalDate periodFrom;
	
	@Column(name = "period_to", nullable = false)	
	private LocalDate periodTo;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "txn_volume")
	private Long txnVolume;
	
	@Column(name = "txn_excess")
	private Long txnExcess;
	
	@Column(name = "fee_amount_excess")
	private Long feeAmountExcess;
	
	@Column(name = "fee_amount_total")
	private Long feeAmountTotal;
	
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	
	@OneToOne
	@JoinColumn(name = "plan_id", nullable = false)
	private Plan plan;
	
	protected ClientBilling() {
		
	}

	public ClientBilling(
			String code, 
			LocalDate periodFrom, 
			LocalDate periodTo, 
			String status, 
			Client client, 
			Plan plan) {
		
		this.code = code;
		this.periodFrom = periodFrom;
		this.periodTo = periodTo;
		this.status = status;
		this.client = client;
		this.plan = plan;
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

	public LocalDate getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(LocalDate periodFrom) {
		this.periodFrom = periodFrom;
	}
	
	public LocalDate getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(LocalDate periodTo) {
		this.periodTo = periodTo;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTxnVolume() {
		return txnVolume;
	}

	public void setTxnVolume(Long txnVolume) {
		this.txnVolume = txnVolume;
	}

	public Long getTxnExcess() {
		return txnExcess;
	}

	public void setTxnExcess(Long txnExcess) {
		this.txnExcess = txnExcess;
	}

	public Long getFeeAmountExcess() {
		return feeAmountExcess;
	}

	public void setFeeAmountExcess(Long feeAmountExcess) {
		this.feeAmountExcess = feeAmountExcess;
	}

	public Long getFeeAmountTotal() {
		return feeAmountTotal;
	}

	public void setFeeAmountTotal(Long feeAmountTotal) {
		this.feeAmountTotal = feeAmountTotal;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
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