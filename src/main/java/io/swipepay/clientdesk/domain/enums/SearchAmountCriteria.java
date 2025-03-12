package io.swipepay.clientdesk.domain.enums;

public enum SearchAmountCriteria {
	isEqualTo("is equal to", "="),
	isGreaterThan("is greater than", ">"),
	isLessThan("is less than", "<");
	
	private final String criteria;
	private final String condition;
	
	private SearchAmountCriteria(String criteria, String condition) {
		this.criteria = criteria;
		this.condition = condition;
	}
	
	public String getCriteria() {
		return criteria;
	}
	
	public String getCondition() {
		return condition;
	}
}