$(document).ready(function() {
	initializeClientKeyForm();
	
	function initializeClientKeyForm() {
		initializeClientKeyCheckboxes();
		initializeClientKeySubmitHandler();
	}
	
	function initializeClientKeyCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientKeySubmitHandler() {
		$('#clientKeyForm').submit(function() {
			$('#clientKeyDimmer').show();
		});
	}
});