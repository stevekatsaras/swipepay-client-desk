package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientCardException;
import io.swipepay.clientdesk.form.wrapper.ClientCardFormWrapper;
import io.swipepay.clientdesk.service.ClientCardService;

@Controller
@RequestMapping(value = "/admin/client/cards")
public class ClientCardController {
	private final String cardsView = "admin/client/cards/view";
	private final String cardsEdit = "admin/client/cards/edit";
	
	@Autowired
	private ClientCardService clientCardService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ClientCardFormWrapper clientCardFormWrapper) {
		clientCardService.init(clientCardFormWrapper);
		return cardsView;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getCards(ClientCardFormWrapper clientCardFormWrapper) {
		clientCardService.init(clientCardFormWrapper);
		return cardsEdit;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editCards(ClientCardFormWrapper clientCardFormWrapper, BindingResult bindingResult) {
		clientCardService.editCards(clientCardFormWrapper);
		return "redirect:/admin/client/cards?updated";
	}
	
	@ExceptionHandler(ClientCardException.class)
	public ModelAndView handle(ClientCardException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}