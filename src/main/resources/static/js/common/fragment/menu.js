$(document).ready(function() {
	initializeMenu();
	
	function initializeMenu() {
		initializeSettingsMenu();
		initializeSignInUserMenu();
	}
	
	function initializeSettingsMenu() {
		$('#transactionsMenuItem').dropdown();
		$('#settingsMenuItem').dropdown();
	}
	
	function initializeSignInUserMenu() {
		$('#signInUserMenuItem').dropdown();
		$('#signOutMenuItem').click(function() {
			$('#logoutForm').submit();
		});
	}
});