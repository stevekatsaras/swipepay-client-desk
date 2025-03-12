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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientTransactionSearchException;
import io.swipepay.clientdesk.exception.ClientTransactionPaymentException;
import io.swipepay.clientdesk.form.ClientTransactionPaymentForm;
import io.swipepay.clientdesk.form.ClientTransactionSearchForm;
import io.swipepay.clientdesk.service.ClientTransactionPaymentService;
import io.swipepay.clientdesk.service.ClientTransactionSearchService;

@Controller
@RequestMapping("/transactions/payment")
@SessionAttributes("clientTransactionPaymentService")
public class ClientTransactionPaymentController {
	private final String transactionsPayment = "transactions/payment/pay";
	private final String transactionsConfirm = "transactions/payment/confirm";
	private final String transactionsReceipt = "transactions/payment/receipt";
	
	@Autowired
	private ClientTransactionPaymentService clientTransactionPaymentService;
	
	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String init(ClientTransactionPaymentForm clientTransactionPaymentForm, ModelMap modelMap) {
		clientTransactionPaymentService.init(clientTransactionPaymentForm, modelMap);
		return transactionsPayment;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
	public String continuePayment(
			@Valid ClientTransactionPaymentForm clientTransactionPaymentForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientTransactionPaymentService.init(clientTransactionPaymentForm, modelMap);
		
		if (bindingResult.hasErrors()) {
			return transactionsPayment;
		}
		
		
		
		
		
		
		return transactionsPayment;
	}
	
//	@RequestMapping(value = "/search", method = RequestMethod.POST)
//	public String search(
//			@Valid ClientTransactionSearchForm clientTransactionSearchForm, 
//			BindingResult bindingResult, 
//			ModelMap modelMap) {
//		
//		if (bindingResult.hasErrors()) {
//			return transactionsSearch;
//		}
//		
//		System.out.println(clientTransactionSearchForm);
//		
//		//clientCustomerService.init(modelMap);
//		return transactionsSearch;
//	}
	
//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
//	public String addCustomer(ClientCustomerForm clientCustomerForm, ModelMap modelMap) {
//		return customersAddView;
//	}
//	
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
//	public String addCustomer(
//			@Valid ClientCustomerForm clientCustomerForm, 
//			BindingResult bindingResult, 
//			ModelMap modelMap) {
//		
//		if (bindingResult.hasErrors()) {
//			return customersAddView;
//		}
//		clientCustomerService.addCustomer(clientCustomerForm);
//		return "redirect:/customers?added";
//	}
//	
//	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
//	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
//	public String getCustomer(@PathVariable Long id, ClientCustomerForm clientCustomerForm, ModelMap modelMap) {
//		clientCustomerService.initCustomer(id, modelMap);
//		clientCustomerService.getCustomer(id, clientCustomerForm);
//		return customersEditView;
//	}
//
//	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
//	@PreAuthorize("hasAnyAuthority('Admin', 'User')")
//	public String editCustomer(
//			@PathVariable Long id, 
//			@Valid ClientCustomerForm clientCustomerForm, 
//			BindingResult bindingResult, 
//			ModelMap modelMap) {
//
//		clientCustomerService.initCustomer(id, modelMap);
//		
//		if (bindingResult.hasErrors()) {
//			return customersEditView;
//		}
//		clientCustomerService.editCustomer(id, clientCustomerForm);
//		return "redirect:/customers?updated";
//	}
	
	@ExceptionHandler(ClientTransactionPaymentException.class)
	public ModelAndView handle(ClientTransactionPaymentException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}	
}