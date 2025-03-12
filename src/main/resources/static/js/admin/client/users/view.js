$(document).ready(function() {
	initializeClientUserTable();
	
	function initializeClientUserTable() {
		initializeClientUserDataTable();
	}
	
	function initializeClientUserDataTable() {
		$('#userTable').DataTable();
	}
});