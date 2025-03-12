$(document).ready(function() {
	initializeClientCustomerCardForm();
	
	function initializeClientCustomerCardForm() {
		initializeClientCustomerCardCheckboxes();
		initializeClientCustomerCardDropdowns();
		initializeClientCustomerCardSubmitHandler();
	}
	
	function initializeClientCustomerCardCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientCustomerCardDropdowns() {
		$('#expiryMonthDropdown').dropdown();
		$('#expiryYearDropdown').dropdown();
	}
	
	function initializeClientCustomerCardSubmitHandler() {
		$('#clientCustomerCardForm').submit(function() {
			$('#clientCustomerCardDimmer').show();
		});
	}
});