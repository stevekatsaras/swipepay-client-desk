$(document).ready(function() {
	initializeClientWalletForm();
	
	function initializeClientWalletForm() {
		initializeClientWalletTab();
		initializeClientWalletDataTable();
	}
	
	function initializeClientWalletTab() {
		$('.tabular.menu .item').tab();
	}
	
	function initializeClientWalletDataTable() {
		$('#walletCardTable').DataTable();
	}
});