$(document).ready(function() {
	initializeClientCurrenciesTable();
	
	function initializeClientCurrenciesTable() {
		initializeClientCurrenciesDataTable();
	}
	
	function initializeClientCurrenciesDataTable() {
		$('#currenciesTable').DataTable({
			"paging": false
		});
	}
});