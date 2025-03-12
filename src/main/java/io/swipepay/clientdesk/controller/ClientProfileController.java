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

import io.swipepay.clientdesk.exception.ClientProfileException;
import io.swipepay.clientdesk.form.ClientProfileForm;
import io.swipepay.clientdesk.service.ClientProfileService;

@Controller
@RequestMapping(value = "/admin/client/profiles")
public class ClientProfileController {
	private final String profilesView = "admin/client/profiles/view";
	private final String profilesAddView = "admin/client/profiles/add";
	private final String profilesEditView = "admin/client/profiles/edit";
	
	@Autowired
	private ClientProfileService clientProfileService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ModelMap modelMap) {
		clientProfileService.init(modelMap);
		return profilesView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String addProfile(ClientProfileForm clientProfileForm, ModelMap modelMap) {
		clientProfileService.initProfile(modelMap);
		return profilesAddView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String addProfile(@Valid ClientProfileForm clientProfileForm, BindingResult bindingResult, ModelMap modelMap) {
		clientProfileService.initProfile(modelMap);
		
		if (bindingResult.hasErrors()) {
			return profilesAddView;
		}
		clientProfileService.addProfile(clientProfileForm);
		return "redirect:/admin/client/profiles?added";
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getProfile(
			@PathVariable String code, 
			ClientProfileForm clientProfileForm, 
			ModelMap modelMap) {
		
		clientProfileService.initProfile(modelMap);
		clientProfileService.getProfile(code, clientProfileForm);
		return profilesEditView;
	}

	@RequestMapping(value = "/edit/{code}", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editProfile(
			@PathVariable String code, 
			@Valid ClientProfileForm clientProfileForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientProfileService.initProfile(modelMap);
		
		if (bindingResult.hasErrors()) {
			return profilesEditView;
		}
		clientProfileService.editProfile(code, clientProfileForm);
		return "redirect:/admin/client/profiles?updated";
	}
	
	@ExceptionHandler(ClientProfileException.class)
	public ModelAndView handle(ClientProfileException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}