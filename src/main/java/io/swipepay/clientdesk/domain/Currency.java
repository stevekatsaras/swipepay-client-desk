package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Currency {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "iso3", nullable = false, unique = true)
	private String iso3;
	
	@Column(name = "symbol", nullable = false)
	private Character symbol;
	
	@Column(name = "symbol_icon")
	private String symbolIcon;
	
	@Column(name = "divisible", nullable = false)
	private Integer divisible;
	
	protected Currency() {
		
	}

	public Currency(String iso3, Character symbol, String symbolIcon, Integer divisible) {
		this.iso3 = iso3;
		this.symbol = symbol;
		this.symbolIcon = symbolIcon;
		this.divisible = divisible;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public Character getSymbol() {
		return symbol;
	}

	public void setSymbol(Character symbol) {
		this.symbol = symbol;
	}

	public String getSymbolIcon() {
		return symbolIcon;
	}

	public void setSymbolIcon(String symbolIcon) {
		this.symbolIcon = symbolIcon;
	}

	public Integer getDivisible() {
		return divisible;
	}

	public void setDivisible(Integer divisible) {
		this.divisible = divisible;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}