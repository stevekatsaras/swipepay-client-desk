package io.swipepay.clientdesk.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.swipepay.clientdesk.exception.ClientPlanException;
import io.swipepay.clientdesk.form.ClientPlanForm;
import io.swipepay.clientdesk.service.ClientPlanService;

@Controller
@RequestMapping(value = "/admin/client/plan")
public class ClientPlanController {
	private final String planView = "admin/client/plan/view";
	
	@Autowired
	private ClientPlanService clientPlanService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String init(ClientPlanForm clientPlanForm) {
		clientPlanService.init(clientPlanForm);
		return planView;
	}
	
	//TODO: need to implement 'change plan' functionality (needs thought)
	
	@ExceptionHandler(ClientPlanException.class)
	public ModelAndView handle(ClientPlanException exception, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("common/pages/error/protected");
	    modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}