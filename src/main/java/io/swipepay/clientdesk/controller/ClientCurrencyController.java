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

import io.swipepay.clientdesk.exception.ClientCurrencyException;
import io.swipepay.clientdesk.form.wrapper.ClientCurrencyFormWrapper;
import io.swipepay.clientdesk.service.ClientCurrencyService;

@Controller
@RequestMapping(value = "/admin/client/currencies")
public class ClientCurrencyController {
	private final String currenciesView = "admin/client/currencies/view";
	private final String currenciesEdit = "admin/client/currencies/edit";
	
	@Autowired
	private ClientCurrencyService clientCurrencyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ClientCurrencyFormWrapper clientCurrencyFormWrapper) {
		clientCurrencyService.init(clientCurrencyFormWrapper);
		return currenciesView;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getCurrencies(ClientCurrencyFormWrapper clientCurrencyFormWrapper) {
		clientCurrencyService.init(clientCurrencyFormWrapper);
		return currenciesEdit;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editCurrencies(ClientCurrencyFormWrapper clientCurrencyFormWrapper, BindingResult bindingResult) {
		clientCurrencyService.editCurrencies(clientCurrencyFormWrapper);
		return "redirect:/admin/client/currencies?updated";
	}
	
	@ExceptionHandler(ClientCurrencyException.class)
	public ModelAndView handle(ClientCurrencyException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}