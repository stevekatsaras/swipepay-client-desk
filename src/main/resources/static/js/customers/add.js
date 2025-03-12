$(document).ready(function() {
	initializeClientCustomerForm();
	
	function initializeClientCustomerForm() {
		initializeClientCustomerCheckboxes();
		initializeClientCustomerSubmitHandler();
	}
	
	function initializeClientCustomerCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientCustomerSubmitHandler() {
		$('#clientCustomerForm').submit(function() {
			$('#clientCustomerDimmer').show();
		});
	}
});