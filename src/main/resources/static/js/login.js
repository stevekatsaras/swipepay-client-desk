$(document).ready(function() {
	initializeLoginForm();
	
	function initializeLoginForm() {
		initializeLoginFields();
		initializeLoginSubmitHandler();
	}
	
	function initializeLoginFields() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeLoginSubmitHandler() {
		$('#loginForm').submit(function() {
			$('#loggingInDimmer').show();
		});
	}
});