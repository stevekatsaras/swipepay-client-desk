package io.swipepay.clientdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationErrorController {
	
	@RequestMapping(value = "/access/denied", method = RequestMethod.GET)
	public String accessDenied() {
		return "common/pages/error/403";
	}
}