$(document).ready(function() {
	initializePlanForm();
	
	function initializePlanForm() {
		initializePlanCarousel();
	}
	
	function initializePlanCarousel() {
		$('#planCarousel').slick({
			arrows: false,
			centerMode: true,
			centerPadding: '60px',
			focusOnSelect: true,
	        slidesToScroll: 1,
			slidesToShow: 1,
			dots: true
		});
		
		$('.plan.button').click(function() {
			var selectedPlanId = $(this).attr('data-value');
			
			$('#planId').val(selectedPlanId);
			doSelectPlanButtonEvent(selectedPlanId, 'click');
		});
		
		doSelectPlanButtonEvent($('#planId').val(), 'init');
	}
	
	function doSelectPlanButtonEvent(selectedPlanId, event) {
		$('.slick-slide').each(function() {
			var aPlanButton = $(this).find('.plan.button');
			var aPlanId = aPlanButton.attr('data-value');
			
			if(selectedPlanId == aPlanId) {
				$(this).addClass('blue');
				aPlanButton.removeClass('secondary').addClass('primary');
				aPlanButton.text('Selected');
				
				if('init' == event) {
					$('#planCarousel').slick('slickGoTo', $(this).attr('data-slick-index'), true);
				}
			}
			else {
				$(this).removeClass('blue');
				aPlanButton.removeClass('primary').addClass('secondary');
				aPlanButton.text('Select');
			}
		});
	}
	
});