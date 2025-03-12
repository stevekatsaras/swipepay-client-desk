$(document).ready(function() {
	initializeClientTransactionPaymentForm();
	
	function initializeClientTransactionPaymentForm() {
		initializeClientTransactionPaymentDropdowns();
	}
	
	function initializeClientTransactionPaymentDropdowns() {
		$('#profileDropdown').dropdown();
		$('#transactionTypeDropdown').dropdown();
		$('#currencyDropdown').dropdown();
		$('#expiryMonthDropdown').dropdown();
		$('#expiryYearDropdown').dropdown();
		
//		$('#cardDropdown').dropdown();
	}
	
});