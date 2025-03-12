package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientAccountException;
import io.swipepay.clientdesk.form.ClientAccountForm;
import io.swipepay.clientdesk.service.ClientAccountService;

@Controller
@RequestMapping(value = "/admin/client/account")
public class ClientAccountController {
	private final String accountView = "admin/client/account/view";
	private final String accountEditView = "admin/client/account/edit";
	
	@Autowired
	private ClientAccountService clientAccountService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ClientAccountForm clientAccountForm) {
		clientAccountService.init(clientAccountForm);
		return accountView;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String editAccount(ClientAccountForm clientAccountForm, ModelMap modelMap) {
		clientAccountService.init(clientAccountForm);
		clientAccountService.init(modelMap);
		return accountEditView;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editAccount(@Valid ClientAccountForm clientAccountForm, BindingResult bindingResult, ModelMap modelMap) {
		clientAccountService.init(modelMap);
		
		if (bindingResult.hasErrors()) {
			return accountEditView;
		}
		clientAccountService.editAccount(clientAccountForm);
		return "redirect:/admin/client/account?updated";
	}
	
	@ExceptionHandler(ClientAccountException.class)
	public ModelAndView handle(ClientAccountException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}