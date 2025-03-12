package io.swipepay.clientdesk.form.wrapper;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swipepay.clientdesk.form.ClientCardForm;

public class ClientCardFormWrapper {
	List<ClientCardForm> clientCardForms;
	
	public List<ClientCardForm> getClientCardForms() {
		return clientCardForms;
	}

	public void setClientCardForms(List<ClientCardForm> clientCardForms) {
		this.clientCardForms = clientCardForms;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}