package io.swipepay.clientdesk.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"code", "client_id"})
})
public class ClientTransaction {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "description", nullable = false)
	private String description;
	
//	@Column(name = "amount", nullable = false)
//	private Long amount;
//	
//	@ManyToOne
//	@JoinColumn(name = "client_currency_id", nullable = false)
//	private ClientCurrency clientCurrecy;
//	
//	@ManyToOne
//	@JoinColumn(name = "transaction_type_id", nullable = false)
//	private TransactionType transactionType;
//	
//	@Column(name = "card_number")
//	private String cardNumber;
//	
//	@Column(name = "card_pan")
//	private String cardPan;
//
//	@Column(name = "expiry_month")
//	private String expiryMonth;
//	
//	@Column(name = "expiry_year")
//	private String expiryYear;
//	
//	@ManyToOne
//	@JoinColumn(name = "client_card_id")
//	public ClientCard clientCard;
//	
//	@ManyToOne
//	@JoinColumn(name = "card_bin_id")
//	private CardBin cardBin;
//	
//	@Column(name = "status", nullable = false)
//	private String status;
//
//	@Column(name = "event", nullable = false)
//	private String event;
//	
//	@ManyToOne
//	@JoinColumn(name = "transaction_response_id")
//	private TransactionResponse transactionResponse;
//	
//	@Column(name = "modified", nullable = false)
//	private LocalDateTime modified;
	
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	
//	@ManyToOne
//	@JoinColumn(name = "client_profile_id", nullable = false)
//	private ClientProfile clientProfile;
//	
//	@ManyToOne
//	@JoinColumn(name = "client_crypto_id")
//	private ClientCrypto clientCrypto;
	
	protected ClientTransaction() {
		
	}
	
	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}