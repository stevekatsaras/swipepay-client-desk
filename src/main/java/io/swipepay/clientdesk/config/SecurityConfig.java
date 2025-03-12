package io.swipepay.clientdesk.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFilter;
import io.swipepay.clientdesk.config.security.DefaultUserDetailsService;
import io.swipepay.clientdesk.support.PasswordSupport;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired 
	private DefaultUserDetailsService defaultDetailsService;
	
	@Autowired
	private PasswordSupport passwordSupport;
	
	private final String LOGIN_URL = "/login";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(defaultAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http
			.authorizeRequests()
				.anyRequest()
					.authenticated()
			.and()
			.formLogin()
				.loginPage(LOGIN_URL)
					.permitAll(true)
			.and()
			.logout()
				.deleteCookies("rememberMe")
					.permitAll(true)
			.and()
			.exceptionHandling()
				.accessDeniedPage("/access/denied")
			.and()
			.sessionManagement()
				.maximumSessions(3);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//static resources
		web.ignoring().antMatchers(
				"/css/**", 
				"/js/**", 
				"/images/**", 
				"/webjars/**");
		
		//controller endpoints
		web.ignoring().antMatchers(
				"/internal-management/**", 
				"/password/**", 
				"/signup/**");
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(defaultDetailsService).passwordEncoder(passwordEncoder());
	}
	
	public PasswordEncoder passwordEncoder() {
		return passwordSupport.getBcryptPasswordEncoder();
	}
	
	public RequestMatcher authenticationRequestMatcher() {
		return new AntPathRequestMatcher(LOGIN_URL, "POST");
	}

	public AuthenticationFailureHandler authenticationFailureHandler() {
		Map<String, String> failureUrlMap = new HashMap<String, String>();
		failureUrlMap.put("org.springframework.security.authentication.CredentialsExpiredException", "/password/change");
		
		ExceptionMappingAuthenticationFailureHandler authenticationFailureHandler = new ExceptionMappingAuthenticationFailureHandler();
		authenticationFailureHandler.setDefaultFailureUrl(LOGIN_URL + "?error");
		authenticationFailureHandler.setExceptionMappings(failureUrlMap);
		return authenticationFailureHandler;
	}

	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler("/");
	}
	
	public DefaultAuthenticationFilter defaultAuthenticationFilter() throws Exception {
		DefaultAuthenticationFilter authenticationFilter = new DefaultAuthenticationFilter();
		authenticationFilter.setRequiresAuthenticationRequestMatcher(authenticationRequestMatcher());
		authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
		authenticationFilter.setAuthenticationManager(super.authenticationManagerBean());
		return authenticationFilter;
	}
	
	/**
	 * Enables concurrent session-control
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
}