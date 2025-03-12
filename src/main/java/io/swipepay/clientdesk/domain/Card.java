package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Card {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "brand", nullable = false)
	private String brand;
	
	@Column(name = "acronym", nullable = false)
	private String acronym;
	
	@Column(name = "icon", nullable = false)
	private String icon;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	protected Card() {
		
	}
	
	public Card(String brand, String acronym, String icon, String type) {
		this.brand = brand;
		this.acronym = acronym;
		this.icon = icon;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}