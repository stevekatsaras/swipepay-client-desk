$(document).ready(function() {
	initializeClientCardsTable();
	
	function initializeClientCardsTable() {
		initializeClientCardsCheckboxes();
		initializeClientCardsSubmitHandler();
	}
	
	function initializeClientCardsCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientCardsSubmitHandler() {
		$('#clientCardFormWrapper').submit(function() {
			$('#clientCardFormWrapperDimmer').show();
		});
	}
});