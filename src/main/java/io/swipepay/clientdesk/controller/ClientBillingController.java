package io.swipepay.clientdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swipepay.clientdesk.service.ClientBillingService;

@Controller
@RequestMapping(value = "/admin/client/billing")
public class ClientBillingController {
	private final String billingView = "admin/client/billing/view";
	
	@Autowired
	private ClientBillingService clientBillingService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ModelMap modelMap) {
		clientBillingService.init(modelMap);
		return billingView;
	}
	
	//TODO: complete the billing functionality
}