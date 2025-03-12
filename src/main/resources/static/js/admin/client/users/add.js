$(document).ready(function() {
	initializeClientUserForm();
	
	function initializeClientUserForm() {
		initializeClientUserCheckboxes();
		initializeClientUserDropdowns();
		initializeClientUserSubmitHandler();
	}
	
	function initializeClientUserCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientUserDropdowns() {
		$('#roleDropdown').dropdown();
	}
	
	function initializeClientUserSubmitHandler() {
		$('#clientUserForm').submit(function() {
			$('#clientUserDimmer').show();
		});
	}
});