package io.swipepay.clientdesk.support;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordSupport {
	/**
	 * The regex below defines the strength that each password must adhere too.
	 * The regex can be understood as:
	 * 
	 * (?=.*[0-9]) a digit must occur at least once
	 * (?=.*[a-z]) a lower case letter must occur at least once
	 * (?=.*[A-Z]) an upper case letter must occur at least once
	 * (?=.*[@#$%^&+=]) a special character must occur at least once
	 * (?=\\S+$) no whitespace allowed in the entire string
	 * .{10,} at least 10 characters
	 */
	private final String passwordStrengthRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{10,}";
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@PostConstruct
	public void initialize() {
		bcryptPasswordEncoder = new BCryptPasswordEncoder();
	}
	
	public String generate() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
	
	public String hash(String clearTextPassword) {
		return bcryptPasswordEncoder.encode(clearTextPassword);
	}
	
	public Boolean isStrong(String clearTextPassword) {
		return clearTextPassword.matches(passwordStrengthRegex);
	}
	
	public Boolean matches(String clearTextPassword, String hashedPassword) {
		return bcryptPasswordEncoder.matches(clearTextPassword, hashedPassword);
	}

	public BCryptPasswordEncoder getBcryptPasswordEncoder() {
		return bcryptPasswordEncoder;
	}
}