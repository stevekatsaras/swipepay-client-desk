package io.swipepay.clientdesk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Offer {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "time", nullable = false)
	private Integer time;

	@Column(name = "unit", nullable = false)
	private String unit;
	
	@Column(name = "message", nullable = false)
	private String message;
	
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	
	protected Offer() {
		
	}

	public Offer(Integer time, String unit, String message, Boolean enabled) {
		this.time = time;
		this.unit = unit;
		this.message = message;
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}