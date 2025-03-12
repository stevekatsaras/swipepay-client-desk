package io.swipepay.clientdesk.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class DefaultAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final String SPRING_SECURITY_FORM_EMAIL_ADDRESS_KEY = "emailAddress";
	
	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(SPRING_SECURITY_FORM_EMAIL_ADDRESS_KEY);
	}
	
	@Override
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain, 
			Authentication authResult) throws IOException, ServletException {
		
		System.out.println("successfulAuthentication()");
		super.successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		//TODO - how should we design brute force attacks??
		
		System.out.println("unsuccessfulAuthentication()");
		super.unsuccessfulAuthentication(request, response, failed);
	}
}