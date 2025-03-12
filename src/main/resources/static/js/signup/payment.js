$(document).ready(function() {
	initializePaymentForm();
	
	function initializePaymentForm() {
		initializePaymentDropdowns();
		initializeCheckboxes();
	}
	
	function initializePaymentDropdowns() {
		$('#expiryMonthDropdown').dropdown();
		$('#expiryYearDropdown').dropdown();
	}
	
	function initializeCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
});