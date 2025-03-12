package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClientBillingForm {
	private String code;
	private String periodFromFormatted;
	private String periodToFormatted;
	private String status;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPeriodFromFormatted() {
		return periodFromFormatted;
	}

	public void setPeriodFromFormatted(String periodFromFormatted) {
		this.periodFromFormatted = periodFromFormatted;
	}

	public String getPeriodToFormatted() {
		return periodToFormatted;
	}

	public void setPeriodToFormatted(String periodToFormatted) {
		this.periodToFormatted = periodToFormatted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}