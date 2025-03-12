$(document).ready(function() {
	initializeClientFraudprotectForm();
	
	function initializeClientFraudprotectForm() {
		initializeClientFraudprotectDropdowns();
		initializeClientFraudprotectSubmitHandler();
	}
	
	function initializeClientFraudprotectDropdowns() {
		//$('#statusDropdown').dropdown();
	}
	
	function initializeClientFraudprotectSubmitHandler() {
		$('#clientFraudprotectForm').submit(function() {
			$('#clientFraudprotectDimmer').show();
		});
	}
});