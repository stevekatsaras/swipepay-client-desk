$(document).ready(function() {
	initializeClientWalletCardForm();
	
	function initializeClientWalletCardForm() {
		initializeClientWalletCardCheckboxes();
		initializeClientWalletCardDropdowns();
		initializeClientWalletCardSubmitHandler();
	}
	
	function initializeClientWalletCardCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientWalletCardDropdowns() {
		$('#expiryMonthDropdown').dropdown();
		$('#expiryYearDropdown').dropdown();
	}
	
	function initializeClientWalletCardSubmitHandler() {
		$('#clientWalletCardForm').submit(function() {
			$('#clientWalletCardDimmer').show();
		});
	}
});