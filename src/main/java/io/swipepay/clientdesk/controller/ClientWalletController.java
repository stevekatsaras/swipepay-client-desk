package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientWalletCardException;
import io.swipepay.clientdesk.service.ClientWalletService;

@Controller
@RequestMapping(value = "/admin/client/wallet")
public class ClientWalletController {
	private final String walletView = "admin/client/wallet/view";
	
	@Autowired
	private ClientWalletService clientWalletService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ModelMap modelMap) {
		clientWalletService.init(modelMap);
		return walletView;
	}
	
	//TODO: how to notify the client when their wallet cards are nearing expiration (ie. 14 days prior)
	
	@ExceptionHandler(ClientWalletCardException.class)
	public ModelAndView handle(ClientWalletCardException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}