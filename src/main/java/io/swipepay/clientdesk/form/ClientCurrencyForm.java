package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClientCurrencyForm {
	private String code;
	private Boolean enabled;
	private String modified;
	private String currencyId;
	private String currencyIso3;
	private Character currencySymbol;
	private String currencySymbolIcon;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyIso3() {
		return currencyIso3;
	}

	public void setCurrencyIso3(String currencyIso3) {
		this.currencyIso3 = currencyIso3;
	}

	public Character getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(Character currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbolIcon() {
		return currencySymbolIcon;
	}

	public void setCurrencySymbolIcon(String currencySymbolIcon) {
		this.currencySymbolIcon = currencySymbolIcon;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}