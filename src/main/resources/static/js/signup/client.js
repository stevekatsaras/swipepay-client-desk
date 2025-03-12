$(document).ready(function() {
	initializeClientForm();
	
	function initializeClientForm() {
		initializeClientDropdowns();
	}
	
	function initializeClientDropdowns() {
		$('#businessEntityDropdown').dropdown();
		$('#countryDropdown').dropdown();
	}
});