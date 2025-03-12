package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ProfileException;
import io.swipepay.clientdesk.form.ProfileForm;
import io.swipepay.clientdesk.service.ProfileService;
import io.swipepay.clientdesk.validator.ProfileValidator;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {
	private final String profileView = "profile/view";
	private final String profileEditView = "profile/edit";
	private final String profilePasswordChangeView = "profile/password/change";
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private ProfileValidator profileValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ProfileForm profileForm) {
		profileService.init(profileForm);
		return profileView;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String getProfile(ProfileForm profileForm) {
		profileService.init(profileForm);
		return profileEditView;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return profileEditView;
		}
		profileService.editProfile(profileForm);
		return "redirect:/profile?updated";
	}
	
	@RequestMapping(value = "/password/change", method = RequestMethod.GET)
	public String changePassword(ProfileForm profileForm) {
		return profilePasswordChangeView;
	}
	
	@RequestMapping(value = "/password/change", method = RequestMethod.POST)
	public String changePassword(
			@Validated(ProfileForm.ValidationPassword.class) ProfileForm profileForm, 
			BindingResult bindingResult) {
		
		profileValidator.validatePasswords(profileForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return profilePasswordChangeView;
		}
		profileService.changePassword(profileForm);
		return "redirect:/profile?changed";
	}
	
	@ExceptionHandler(ProfileException.class)
	public ModelAndView handle(ProfileException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}