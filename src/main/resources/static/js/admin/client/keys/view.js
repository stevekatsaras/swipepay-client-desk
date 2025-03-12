$(document).ready(function() {
	initializeClientKeyTable();
	
	function initializeClientKeyTable() {
		initializeClientKeyTab();
		initializeClientKeyButtons();
		setClientKeyTab();
	}
	
	function initializeClientKeyTab() {
		$('.tabular.menu .item').tab({
			onLoad: function(tabPath) {
				if ('pubkeys' == tabPath) {
					$('#pubkeyTable').DataTable();
				}
			}
		});
	}
	
	function initializeClientKeyButtons() {
		$('#resetPrivateKeyButton').click(function() {
			$('#resetPrivateKeyModal').modal({
				onApprove : function() {
					window.location.href = $('#resetPrivateKeyButton').data('uri');
				}
			}).modal('show');
		});
	}
	
	function setClientKeyTab() {
		var tabName = getUrlRequestParam('tab');
		if (tabName != null) {
			$('.tabular.menu .item').tab('change tab', tabName[1]);
		}
		else {
			$('.tabular.menu .item').tab('change tab', 'pubkeys');
		}
	}
});