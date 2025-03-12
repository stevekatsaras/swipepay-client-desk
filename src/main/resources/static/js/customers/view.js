$(document).ready(function() {
	initializeClientCustomerTable();
	
	function initializeClientCustomerTable() {
		initializeClientCustomerDataTable();
	}
	
	function initializeClientCustomerDataTable() {
		$('#customerTable').DataTable();
	}
});