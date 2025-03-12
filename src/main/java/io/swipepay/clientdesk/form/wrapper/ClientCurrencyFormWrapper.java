package io.swipepay.clientdesk.form.wrapper;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swipepay.clientdesk.form.ClientCurrencyForm;

public class ClientCurrencyFormWrapper {
	List<ClientCurrencyForm> clientCurrencyForms;

	public List<ClientCurrencyForm> getClientCurrencyForms() {
		return clientCurrencyForms;
	}

	public void setClientCurrencyForms(List<ClientCurrencyForm> clientCurrencyForms) {
		this.clientCurrencyForms = clientCurrencyForms;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}