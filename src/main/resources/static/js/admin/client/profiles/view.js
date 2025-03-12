$(document).ready(function() {
	initializeClientProfileTable();
	
	function initializeClientProfileTable() {
		initializeClientProfileDataTable();
	}
	
	function initializeClientProfileDataTable() {
		$('#profileTable').DataTable();
	}
});