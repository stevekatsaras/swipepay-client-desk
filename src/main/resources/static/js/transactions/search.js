$(document).ready(function() {
	initializeClientTransactionTable();
	
	function initializeClientTransactionTable() {
		initializeClientTransactionDropdowns();
		initializeClientTransactionAccordion();
	}
	
	function initializeClientTransactionDropdowns() {
		$('#profilesDropdown').dropdown();
		$('#transactionTypesDropdown').dropdown();
		$('#amountCriteriaDropdown').dropdown();
		$('#currenciesDropdown').dropdown();
		$('#expiryMonthDropdown').dropdown();
//		$('#cardDropdown').dropdown();
//		$('#statusDropdown').dropdown();
//		$('#eventDropdown').dropdown();
//		$('#transactionResponseDropdown').dropdown();
	}
	
	function initializeClientTransactionAccordion() {
		$('.ui.accordion').accordion();
	}
});