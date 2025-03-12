package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swipepay.clientdesk.exception.SignupException;
import io.swipepay.clientdesk.form.SignupForm;
import io.swipepay.clientdesk.service.SignupService;
import io.swipepay.clientdesk.validator.SignupValidator;

@Controller
@RequestMapping(value = "/signup")
@SessionAttributes("signupForm")
public class SignupController {
	private final String userView = "signup/user";
	private final String clientView = "signup/client";
	private final String planView = "signup/plan";
	private final String paymentView = "signup/payment";
	private final String completedView = "signup/completed";
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private SignupValidator signupValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(SignupForm signupForm, SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/signup/user";
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user(SignupForm signupForm) {
		return userView;
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String user(
			@Validated(SignupForm.ValidationUser.class) SignupForm signupForm, 
			BindingResult bindingResult) {
		
		signupValidator.validateUser(signupForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return userView;
		}
		return "redirect:/signup/client";
	}
	
	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String client(SignupForm signupForm, ModelMap modelMap) {
		signupService.initClient(modelMap);
		return clientView;
	}
	
	@RequestMapping(value = "/client", method = RequestMethod.POST)
	public String client(
			@Validated(SignupForm.ValidationClient.class) SignupForm signupForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		signupService.initClient(modelMap);
		
		if (bindingResult.hasErrors()) {
			return clientView;
		}
		return "redirect:/signup/plan";
	}
	
	@RequestMapping(value = "/plan", method = RequestMethod.GET)
	public String plan(SignupForm signupForm, ModelMap modelMap) {
		signupService.initPlan(modelMap);
		return planView;
	}
	
	@RequestMapping(value = "/plan", method = RequestMethod.POST)
	public String plan(
			@Validated(SignupForm.ValidationPlan.class) SignupForm signupForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		signupService.initPlan(modelMap);
		
		signupValidator.validatePlan(signupForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return planView;
		}
		return "redirect:/signup/payment";
	}
	
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(SignupForm signupForm, ModelMap modelMap) {
		signupService.initPayment(signupForm, modelMap);
		return paymentView;
	}
	
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String payment(
			@Validated(SignupForm.ValidationPayment.class) SignupForm signupForm, 
			BindingResult bindingResult, 
			ModelMap modelMap, 
			SessionStatus sessionStatus, 
			RedirectAttributes redirectAttributes) {
		
		signupService.initPayment(signupForm, modelMap);
		
		signupValidator.validateCard(signupForm, bindingResult, modelMap);
		signupValidator.validateExpiry(signupForm, bindingResult);
		signupValidator.validateCvv(signupForm, bindingResult, modelMap);
		if (bindingResult.hasErrors()) {
			return paymentView;
		}
		
		//TODO: should we perform a pre-auth on the card?
		
		signupService.signup(signupForm, modelMap);
		sessionStatus.setComplete();
		
		redirectAttributes.addFlashAttribute("firstname", signupForm.getFirstname());
		return "redirect:/signup/completed";
	}
	
	@RequestMapping(value = "/completed", method = RequestMethod.GET)
	public String completed() {
		return completedView;
	}
	
	@ExceptionHandler(SignupException.class)
	public ModelAndView handle(SignupException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/public");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}