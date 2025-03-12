$(document).ready(function() {
	initializeClientFraudprotectTable();
	
	function initializeClientFraudprotectTable() {
		initializeClientFraudprotectDataTable();
	}
	
	function initializeClientFraudprotectDataTable() {
		$('#fraudprotectTable').DataTable();
	}
});