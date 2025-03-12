$(document).ready(function() {
	initializeClientAccountForm();
	
	function initializeClientAccountForm() {
		initializeClientAccountDropdowns();
		initializeClientAccountFormSubmitHandler();
	}
	
	function initializeClientAccountDropdowns() {
		$('#businessEntityDropdown').dropdown();
		$('#countryDropdown').dropdown();
	}
	
	function initializeClientAccountFormSubmitHandler() {
		$('#clientAccountForm').submit(function() {
			$('#clientAccountDimmer').show();
		});
	}
});