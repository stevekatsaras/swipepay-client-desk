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
import io.swipepay.clientdesk.exception.ClientKeyException;
import io.swipepay.clientdesk.form.ClientKeyForm;
import io.swipepay.clientdesk.form.ClientPrivateKeyForm;
import io.swipepay.clientdesk.service.ClientKeyService;

@Controller
@RequestMapping(value = "/admin/client/keys")
public class ClientKeyController {
	private final String keysView = "admin/client/keys/view";
	private final String keysPublicEditView = "admin/client/keys/public/edit";
	
	@Autowired
	private ClientKeyService clientKeyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ClientPrivateKeyForm clientPrivateKeyForm, ModelMap modelMap) {
		clientKeyService.initPublicKeys(modelMap);
		clientKeyService.initPrivateKey(clientPrivateKeyForm);
		return keysView;
	}
	
	@RequestMapping(value = "/public/add", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String addPublicKey() {
		clientKeyService.addPublicKey();
		return "redirect:/admin/client/keys?added";
	}
	
	@RequestMapping(value = "/public/edit/{code}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getPublicKey(
			@PathVariable String code, 
			ClientKeyForm clientKeyForm) {
		
		clientKeyService.getPublicKey(code, clientKeyForm);
		return keysPublicEditView;
	}

	@RequestMapping(value = "/public/edit/{code}", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editPublicKey(
			@PathVariable String code, 
			@Valid ClientKeyForm clientKeyForm, 
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return keysPublicEditView;
		}
		clientKeyService.editPublicKey(code, clientKeyForm);
		return "redirect:/admin/client/keys?updated";
	}
	
	@RequestMapping(value = "/private/reset", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String resetPrivateKey() {
		clientKeyService.resetPrivateKey();
		return "redirect:/admin/client/keys?reset&tab=privkey";
	}
	
	@ExceptionHandler(ClientKeyException.class)
	public ModelAndView handle(ClientKeyException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}