package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.PasswordException;
import io.swipepay.clientdesk.form.PasswordForm;
import io.swipepay.clientdesk.service.PasswordService;
import io.swipepay.clientdesk.validator.PasswordValidator;

@Controller
@RequestMapping(value = "/password")
public class PasswordController {
	private final String resetView = "password/reset/reset";
	private final String resetCompletedView = "password/reset/completed";
	
	private final String changeView = "password/change/change";
	private final String changeCompletedView = "password/change/completed";
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private PasswordValidator passwordValidator;
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String reset(PasswordForm passwordForm) {
		return resetView;
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String reset(
			@Validated(PasswordForm.ValidationReset.class) PasswordForm passwordForm, 
			BindingResult bindingResult) {
		
		passwordValidator.validateResetPassword(passwordForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return resetView;
		}
		passwordService.reset(passwordForm);
		return "redirect:/password/reset/completed";
	}
	
	@RequestMapping(value = "/reset/completed", method = RequestMethod.GET)
	public String resetCompleted() {
		return resetCompletedView;
	}
	
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String change(PasswordForm passwordForm) {
		return changeView;
	}
	
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public String change(
			@Validated(PasswordForm.ValidationChange.class) PasswordForm passwordForm, 
			BindingResult bindingResult) {
		
		passwordValidator.validateChangePassword(passwordForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return changeView;
		}
		passwordService.change(passwordForm);
		return "redirect:/password/change/completed";
	}
	
	@RequestMapping(value = "/change/completed", method = RequestMethod.GET)
	public String changeCompleted() {
		return changeCompletedView;
	}
	
	@ExceptionHandler(PasswordException.class)
	public ModelAndView handle(PasswordException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/public");
		modelAndView.addObject("message", exception.getMessage());
		modelAndView.addObject("requestUri", request.getRequestURI());
		return modelAndView;
	}
}