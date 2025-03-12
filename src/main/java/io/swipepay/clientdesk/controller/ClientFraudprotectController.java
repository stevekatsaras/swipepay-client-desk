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

import io.swipepay.clientdesk.exception.ClientFraudprotectException;
import io.swipepay.clientdesk.form.ClientFraudprotectForm;
import io.swipepay.clientdesk.service.ClientFraudprotectService;

@Controller
@RequestMapping(value = "/admin/client/fraudprotect")
public class ClientFraudprotectController {
	private final String fraudprotectView = "admin/client/fraudprotect/view";
	private final String fraudprotectAddView = "admin/client/fraudprotect/add";
	private final String fraudprotectEditView = "admin/client/fraudprotect/edit";
	
	@Autowired
	private ClientFraudprotectService clientFraudprotectService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ModelMap modelMap) {
		clientFraudprotectService.init(modelMap);
		return fraudprotectView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String addFraudprotect(ClientFraudprotectForm clientFraudprotectForm, ModelMap modelMap) {
		clientFraudprotectService.initFraudprotect(modelMap);
		return fraudprotectAddView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String addFraudprotect(
			@Valid ClientFraudprotectForm clientFraudprotectForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientFraudprotectService.initFraudprotect(modelMap);
		
		if (bindingResult.hasErrors()) {
			return fraudprotectAddView;
		}
		clientFraudprotectService.addFraudprotect(clientFraudprotectForm);
		return "redirect:/admin/client/fraudprotect?added";
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getFraudprotect(
			@PathVariable String code, 
			ClientFraudprotectForm clientFraudprotectForm, 
			ModelMap modelMap) {
		
		clientFraudprotectService.initFraudprotect(modelMap);
		clientFraudprotectService.getFraudprotect(code, clientFraudprotectForm);
		return fraudprotectEditView;
	}

	@RequestMapping(value = "/edit/{code}", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editFraudprotect(
			@PathVariable String code, 
			@Valid ClientFraudprotectForm clientFraudprotectForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientFraudprotectService.initFraudprotect(modelMap);
		
		if (bindingResult.hasErrors()) {
			return fraudprotectEditView;
		}
		clientFraudprotectService.editFraudprotect(code, clientFraudprotectForm);
		return "redirect:/admin/client/fraudprotect?updated";
	}
	
	@ExceptionHandler(ClientFraudprotectException.class)
	public ModelAndView handle(ClientFraudprotectException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}