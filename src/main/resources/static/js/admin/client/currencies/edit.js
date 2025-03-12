$(document).ready(function() {
	initializeClientCurrenciesTable();
	
	function initializeClientCurrenciesTable() {
		initializeClientCurrenciesCheckboxes();
		initializeClientCurrenciesSubmitHandler();
	}
	
	function initializeClientCurrenciesCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientCurrenciesSubmitHandler() {
		$('#clientCurrencyFormWrapper').submit(function() {
			$('#clientCurrencyFormWrapperDimmer').show();
		});
	}
});