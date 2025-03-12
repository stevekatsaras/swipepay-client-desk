package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientCustomerException;
import io.swipepay.clientdesk.form.ClientCustomerForm;
import io.swipepay.clientdesk.service.ClientCustomerService;

@Controller
@RequestMapping("/customers")
public class ClientCustomerController {
	private final String customersView = "customers/view";
	private final String customersAddView = "customers/add";
	private final String customersEditView = "customers/edit";
	
	@Autowired
	private ClientCustomerService clientCustomerService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ModelMap modelMap) {
		clientCustomerService.init(modelMap);
		return customersView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String addCustomer(ClientCustomerForm clientCustomerForm, ModelMap modelMap) {
		return customersAddView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String addCustomer(
			@Valid ClientCustomerForm clientCustomerForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		if (bindingResult.hasErrors()) {
			return customersAddView;
		}
		clientCustomerService.addCustomer(clientCustomerForm);
		return "redirect:/customers?added";
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String getCustomer(
			@PathVariable String code, 
			ClientCustomerForm clientCustomerForm, 
			ModelMap modelMap) {
		
		clientCustomerService.initCustomer(code, modelMap);
		clientCustomerService.getCustomer(code, clientCustomerForm);
		return customersEditView;
	}

	@RequestMapping(value = "/edit/{code}", method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String editCustomer(
			@PathVariable String code, 
			@Valid ClientCustomerForm clientCustomerForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {

		clientCustomerService.initCustomer(code, modelMap);
		
		if (bindingResult.hasErrors()) {
			return customersEditView;
		}
		clientCustomerService.editCustomer(code, clientCustomerForm);
		return "redirect:/customers?updated";
	}
	
	@ExceptionHandler(ClientCustomerException.class)
	public ModelAndView handle(ClientCustomerException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}	
}