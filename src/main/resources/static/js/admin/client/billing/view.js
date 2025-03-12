$(document).ready(function() {
	initializeClientBillingTable();
	
	function initializeClientBillingTable() {
		initializeClientBillingDataTable();
	}
	
	function initializeClientBillingDataTable() {
		$('#billingTable').DataTable();
	}
});