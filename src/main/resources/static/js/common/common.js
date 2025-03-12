$(document).ready(function() {
	initialize();
	
	function initialize() {
		initializeBody();
		initializeDismissableMessages();
		initializeHyperlinkButtons();
	}
	
	function initializeBody() {
		if ($('body').attr('class') == 'random background') {
			var images = [
				'bg-1.jpg', 
				'bg-2.jpg', 
				'bg-3.jpg', 
				'bg-4.jpg', 
				'bg-5.jpg', 
				'bg-6.jpg', 
				'bg-7.jpg', 
				'bg-8.jpg', 
				'bg-9.jpg', 
				'bg-10.jpg', 
				'bg-11.jpg', 
				'bg-12.jpg', 
				'bg-13.jpg', 
				'bg-14.jpg', 
				'bg-15.jpg', 
				'bg-16.jpg', 
				'bg-17.jpg', 
				'bg-18.jpg', 
				'bg-19.jpg', 
				'bg-20.jpg', 
				'bg-21.jpg', 
				'bg-22.jpg', 
				'bg-23.jpg', 
				'bg-24.jpg', 
				'bg-25.jpg'
			];
			$('body').css({'background' : 'url(/images/' + images[Math.floor(Math.random() * images.length)] + ') no-repeat center center fixed'});			
		}
	}
	
	function initializeDismissableMessages() {
		$('.message .close').on('click', function() {
			$(this).closest('.message').transition('fade');
		});
	}
	
	function initializeHyperlinkButtons() {
		$('.hyperlink.button').on('click', function() {
			window.location.href = $(this).data('uri');
		});
	}
});

/**
 * The following are global default methods that are accessible from any page in this site.
 * They do not use jQuery.
 */

function getUrlRequestParam(sParam) {
	var sPageUrl = window.location.search.substring(1);
	var sUrlParameters = sPageUrl.split('&');
	
	for (var i = 0; i < sUrlParameters.length; i++) {
		var sParameterName = sUrlParameters[i].split('=');
		if (sParameterName[0] == sParam) {
			return sParameterName;
		}
	}
}