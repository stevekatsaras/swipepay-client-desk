$(document).ready(function() {
	initializeSignupForm();
	
	function initializeSignupForm() {
		initializeSignupSubmitHandler();
	}
	
	function initializeSignupSubmitHandler() {
		$('#signupForm').submit(function() {
			$('#signupDimmer').show();
		});
	}
});