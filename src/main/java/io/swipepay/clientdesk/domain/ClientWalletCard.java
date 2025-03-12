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
		@UniqueConstraint(columnNames = {"code", "client_wallet_id"})
})
public class ClientWalletCard {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "cardholder_name", nullable = false)
	private String cardHolderName;

	@Column(name = "cardholder_email", nullable = false)
	private String cardHolderEmail;
	
	@Column(name = "card_number", nullable = false)
	private String cardNumber;
	
	@Column(name = "card_pan", nullable = false)
	private String cardPan;
	
	@Column(name = "expiry_month", nullable = false)
	private String expiryMonth;
	
	@Column(name = "expiry_year", nullable = false)
	private String expiryYear;
	
	@ManyToOne
	@JoinColumn(name = "card_id", nullable = false)
	private Card card;
	
	@ManyToOne
	@JoinColumn(name = "card_bin_id", nullable = false)
	private CardBin cardBin;
	
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

	@Column(name = "is_default", nullable = false)
	private Boolean isDefault;
	
	@Column(name = "modified", nullable = false)
	private LocalDateTime modified;
	
	@ManyToOne
	@JoinColumn(name = "client_wallet_id", nullable = false)
	private ClientWallet clientWallet;
	
	@ManyToOne
	@JoinColumn(name = "client_crypto_id", nullable = false)
	private ClientCrypto clientCrypto;
	
	protected ClientWalletCard() {
		
	}
	
	public ClientWalletCard(
			String code, 
			String cardHolderName, 
			String cardHolderEmail, 
			String cardNumber, 
			String cardPan, 
			String expiryMonth, 
			String expiryYear, 
			Card card,
			CardBin cardBin, 
			Boolean enabled, 
			Boolean isDefault, 
			LocalDateTime modified,
			ClientWallet clientWallet, 
			ClientCrypto clientCrypto) {
		
		this.code = code;
		this.cardHolderName = cardHolderName;
		this.cardHolderEmail = cardHolderEmail;
		this.cardNumber = cardNumber;
		this.cardPan = cardPan;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
		this.card = card;
		this.cardBin = cardBin;
		this.enabled = enabled;
		this.isDefault = isDefault;
		this.modified = modified;
		this.clientWallet = clientWallet;
		this.clientCrypto = clientCrypto;
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

	public String getCardPan() {
		return cardPan;
	}

	public void setCardPan(String cardPan) {
		this.cardPan = cardPan;
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

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public CardBin getCardBin() {
		return cardBin;
	}

	public void setCardBin(CardBin cardBin) {
		this.cardBin = cardBin;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public ClientWallet getClientWallet() {
		return clientWallet;
	}

	public void setClientWallet(ClientWallet clientWallet) {
		this.clientWallet = clientWallet;
	}

	public ClientCrypto getClientCrypto() {
		return clientCrypto;
	}

	public void setClientCrypto(ClientCrypto clientCrypto) {
		this.clientCrypto = clientCrypto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}