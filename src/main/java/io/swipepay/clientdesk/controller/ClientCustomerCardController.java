package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientCustomerCardException;
import io.swipepay.clientdesk.form.ClientCustomerCardForm;
import io.swipepay.clientdesk.service.ClientCustomerCardService;
import io.swipepay.clientdesk.validator.ClientCustomerCardValidator;

@Controller
@RequestMapping(value = "/customers/{customerCode}/card")
public class ClientCustomerCardController {
	private final String customerCardAddView = "customers/card/add";
	private final String customerCardEditView = "customers/card/edit";
	
	@Autowired
	private ClientCustomerCardService clientCustomerCardService;
	
	@Autowired
	private ClientCustomerCardValidator clientCustomerCardValidator;
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String addCustomerCard(
			@PathVariable String customerCode, 
			ClientCustomerCardForm clientCustomerCardForm, 
			ModelMap modelMap) {
		
		clientCustomerCardService.init(customerCode, modelMap);
		return customerCardAddView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String addCustomerCard(
			@PathVariable String customerCode, 
			@Validated(ClientCustomerCardForm.ValidationAdd.class) ClientCustomerCardForm clientCustomerCardForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {

		clientCustomerCardService.init(customerCode, modelMap);
		
		clientCustomerCardValidator.validateClientCard(clientCustomerCardForm, bindingResult, modelMap);
		clientCustomerCardValidator.validateExpiry(clientCustomerCardForm, bindingResult);
		clientCustomerCardValidator.validateIsDefaultAllowedOnDisabledCard(clientCustomerCardForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return customerCardAddView;
		}
		clientCustomerCardService.addCustomerCard(customerCode, clientCustomerCardForm, modelMap);
		return "redirect:/customers/edit/" + customerCode + "?cardAdded&tab=cards";
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String getCustomerCard(
			@PathVariable String customerCode, 
			@PathVariable String code, 
			ClientCustomerCardForm clientCustomerCardForm, 
			ModelMap modelMap) {

		clientCustomerCardService.init(customerCode, modelMap);
		clientCustomerCardService.getCustomerCard(customerCode, code, clientCustomerCardForm);
		return customerCardEditView;
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String editCustomerCard(
			@PathVariable String customerCode, 
			@PathVariable String code, 
			@Validated(ClientCustomerCardForm.ValidationEdit.class) ClientCustomerCardForm clientCustomerCardForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientCustomerCardService.init(customerCode, modelMap);

		clientCustomerCardValidator.validateClientCard(clientCustomerCardForm, bindingResult, modelMap);
		clientCustomerCardValidator.validateExpiry(clientCustomerCardForm, bindingResult);
		clientCustomerCardValidator.validateIsDefaultAllowedOnDisabledCard(clientCustomerCardForm, bindingResult);
		clientCustomerCardValidator.validateIsNoDefaultAllowedOnSingleCard(customerCode, clientCustomerCardForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return customerCardEditView;
		}
		clientCustomerCardService.editCustomerCard(customerCode, code, clientCustomerCardForm, modelMap);
		return "redirect:/customers/edit/" + customerCode + "?cardUpdated&tab=cards";
	}
	
	@ExceptionHandler(ClientCustomerCardException.class)
	public ModelAndView handle(ClientCustomerCardException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}