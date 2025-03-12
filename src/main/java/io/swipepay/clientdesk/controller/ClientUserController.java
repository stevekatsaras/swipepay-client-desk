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

import io.swipepay.clientdesk.exception.ClientUserException;
import io.swipepay.clientdesk.form.ClientUserForm;
import io.swipepay.clientdesk.service.ClientUserService;
import io.swipepay.clientdesk.validator.ClientUserValidator;

@Controller
@RequestMapping(value = "/admin/client/users")
public class ClientUserController {
	private final String usersView = "admin/client/users/view";
	private final String usersAddView = "admin/client/users/add";
	private final String usersEditView = "admin/client/users/edit";
	
	@Autowired
	private ClientUserService clientUserService;
	
	@Autowired
	private ClientUserValidator clientUserValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ModelMap modelMap) {
		clientUserService.init(modelMap);
		return usersView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String addUser(ClientUserForm clientUserForm, ModelMap modelMap) {
		clientUserService.initUser(modelMap);
		return usersAddView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String addUser(
			@Valid ClientUserForm clientUserForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientUserService.initUser(modelMap);
		
		clientUserValidator.validateIfEmailAddressIsRegistered(clientUserForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return usersAddView;
		}
		clientUserService.addUser(clientUserForm);
		return "redirect:/admin/client/users?added";
	}
	
	@RequestMapping(value = "/edit/{emailAddress:.+}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getUser(
			@PathVariable String emailAddress, 
			ClientUserForm clientUserForm, 
			ModelMap modelMap) {
		
		clientUserService.initUser(modelMap);
		clientUserService.getUser(emailAddress, clientUserForm);
		return usersEditView;
	}

	@RequestMapping(value = "/edit/{emailAddress:.+}", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editUser(
			@PathVariable String emailAddress, 
			@Valid ClientUserForm clientUserForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientUserService.initUser(modelMap);
		
		clientUserValidator.validateIfDisabledUserIsPrincipal(clientUserForm, bindingResult, emailAddress);
		if (bindingResult.hasErrors()) {
			return usersEditView;
		}
		
		clientUserService.editUser(emailAddress, clientUserForm);
		return "redirect:/admin/client/users?updated";
	}
	
	@ExceptionHandler(ClientUserException.class)
	public ModelAndView handle(ClientUserException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}