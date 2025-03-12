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

import io.swipepay.clientdesk.exception.ClientWalletCardException;
import io.swipepay.clientdesk.form.ClientWalletCardForm;
import io.swipepay.clientdesk.service.ClientWalletCardService;
import io.swipepay.clientdesk.validator.ClientWalletCardValidator;

@Controller
@RequestMapping(value = "/admin/client/wallet/card")
public class ClientWalletCardController {
	private final String walletCardAddView = "admin/client/wallet/card/add";
	private final String walletCardEditView = "admin/client/wallet/card/edit";
	
	@Autowired
	private ClientWalletCardService clientWalletCardService;
	
	@Autowired
	private ClientWalletCardValidator clientWalletCardValidator;
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String addWalletCard(ClientWalletCardForm clientWalletCardForm, ModelMap modelMap) {
		clientWalletCardService.init(modelMap);
		return walletCardAddView;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String addWalletCard(
			@Validated(ClientWalletCardForm.ValidationAdd.class) ClientWalletCardForm clientWalletCardForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientWalletCardService.init(modelMap);
		
		clientWalletCardValidator.validateCard(clientWalletCardForm, bindingResult, modelMap);
		clientWalletCardValidator.validateExpiry(clientWalletCardForm, bindingResult);
		clientWalletCardValidator.validateIsDefaultAllowedOnDisabledCard(clientWalletCardForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return walletCardAddView;
		}
		
		//TODO: perhaps we need to do a preauth on the card!!
		
		clientWalletCardService.addWalletCard(clientWalletCardForm, modelMap);
		return "redirect:/admin/client/wallet?cardAdded";
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('Admin')")
	public String getWalletCard(
			@PathVariable String code, 
			ClientWalletCardForm clientWalletCardForm, 
			ModelMap modelMap) {
		
		clientWalletCardService.init(modelMap);
		clientWalletCardService.getWalletCard(code, clientWalletCardForm);
		return walletCardEditView;
	}
	
	@RequestMapping(value = "/edit/{code}", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('Admin')")
	public String editWalletCard(
			@PathVariable String code, 
			@Validated(ClientWalletCardForm.ValidationEdit.class) ClientWalletCardForm clientWalletCardForm, 
			BindingResult bindingResult, 
			ModelMap modelMap) {
		
		clientWalletCardService.init(modelMap);
		
		clientWalletCardValidator.validateCard(clientWalletCardForm, bindingResult, modelMap);
		clientWalletCardValidator.validateExpiry(clientWalletCardForm, bindingResult);		
		clientWalletCardValidator.validateIsDefaultAllowedOnDisabledCard(clientWalletCardForm, bindingResult);
		clientWalletCardValidator.validateIsNoDefaultAllowedOnSingleCard(clientWalletCardForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return walletCardEditView;
		}
		
		//TODO: do we need to do preauth on this card? it's existing! we may have to if the card number is altered!
		
		clientWalletCardService.editWalletCard(code, clientWalletCardForm, modelMap);
		return "redirect:/admin/client/wallet?cardUpdated";
	}
	
	@ExceptionHandler(ClientWalletCardException.class)
	public ModelAndView handle(ClientWalletCardException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}