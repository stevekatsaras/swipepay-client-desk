$(document).ready(function() {
	initializePasswordForm();
	
	function initializePasswordForm() {
		initializePasswordSubmitHandler();
	}
	
	function initializePasswordSubmitHandler() {
		$('#passwordForm').submit(function() {
			$('#passwordDimmer').show();
		});
	}
});