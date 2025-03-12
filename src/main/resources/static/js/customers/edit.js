$(document).ready(function() {
	initializeClientCustomerForm();
	
	function initializeClientCustomerForm() {
		initializeClientCustomerTab();
		initializeClientCustomerCheckboxes();
		initializeClientCustomerSubmitHandler();
		setClientCustomerTab();
	}
	
	function initializeClientCustomerTab() {
		$('.tabular.menu .item').tab({
			onLoad: function(tabPath) {
				if ('cards' == tabPath) {
					$('#customerCardTable').DataTable();
				}
			}
		});
	}
	
	function initializeClientCustomerDataTable() {
		$('#customerCardTable').DataTable();
	}
	
	function initializeClientCustomerCheckboxes() {
		$('.ui.checkbox').checkbox();
	}
	
	function initializeClientCustomerSubmitHandler() {
		$('#clientCustomerForm').submit(function() {
			$('#clientCustomerDimmer').show();
		});
	}
	
	function setClientCustomerTab() {
		var tabName = getUrlRequestParam('tab');
		if (tabName != null) {
			$('.tabular.menu .item').tab('change tab', tabName[1]);			
		}
		else {
			$('.tabular.menu .item').tab('change tab', 'customer');
		}
	}
});