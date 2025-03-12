$(document).ready(function() {
	initializeClientFraudprotectForm();
	
	function initializeClientFraudprotectForm() {
		initializeClientFraudprotectDropdowns();
		initializeClientFraudprotectSubmitHandler();
	}
	
	function initializeClientProfileDropdowns() {
		//$('#statusDropdown').dropdown();
	}
	
	function initializeClientFraudprotectSubmitHandler() {
		$('#clientFraudprotectForm').submit(function() {
			$('#clientFraudprotectDimmer').show();
		});
	}
});