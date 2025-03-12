$(document).ready(function() {
	initializeClientCardsTable();
	
	function initializeClientCardsTable() {
		initializeClientCardsDataTable();
	}
	
	function initializeClientCardsDataTable() {
		$('#cardsTable').DataTable({
			"paging": false
		});
	}
});