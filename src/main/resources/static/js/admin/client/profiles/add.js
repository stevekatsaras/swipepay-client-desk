$(document).ready(function() {
	initializeClientProfileForm();
	
	function initializeClientProfileForm() {
		initializeClientProfileCheckboxes();
		initializeClientProfileDropdowns();
		initializeClientProfileSubmitHandler();
	}
	
	function initializeClientProfileCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientProfileDropdowns() {
		$('#statusDropdown').dropdown();
	}
	
	function initializeClientProfileSubmitHandler() {
		$('#clientProfileForm').submit(function() {
			$('#clientProfileDimmer').show();
		});
	}
});