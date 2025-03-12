$(document).ready(function() {
	initializeProfileForm();
	
	function initializeProfileForm() {
		initializeProfileSubmitHandler();
	}
	
	function initializeProfileSubmitHandler() {
		$('#profileForm').submit(function() {
			$('#profileDimmer').show();
		});
	}
});