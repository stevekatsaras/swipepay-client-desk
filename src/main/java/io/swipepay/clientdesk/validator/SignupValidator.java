package io.swipepay.clientdesk.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.CardBin;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.domain.Plan;
import io.swipepay.clientdesk.domain.enums.CardBrand;
import io.swipepay.clientdesk.domain.enums.PlanCostType;
import io.swipepay.clientdesk.domain.enums.PlanType;
import io.swipepay.clientdesk.exception.SignupException;
import io.swipepay.clientdesk.form.SignupForm;
import io.swipepay.clientdesk.repository.CardBinRepository;
import io.swipepay.clientdesk.repository.ClientUserRepository;
import io.swipepay.clientdesk.repository.PlanRepository;
import io.swipepay.clientdesk.support.CardSupport;
import io.swipepay.clientdesk.support.PasswordSupport;

@Component
public class SignupValidator implements Validator {
	@Autowired
	private CardBinRepository cardBinRepository;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Autowired
	private PasswordSupport passwordSupport;
	
	@Autowired
	private PlanRepository planRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return SignupValidator.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}
	
	public void validateUser(Object target, Errors errors) throws SignupException {
		SignupForm signupForm = (SignupForm) target;
		try {
			validateIfEmailAddressIsRegistered(signupForm, errors);
			validatePassword(signupForm, errors);
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to validate your email address and/or password.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void validatePlan(Object target, Errors errors) throws SignupException {
		SignupForm signupForm = (SignupForm) target;
		try {
			if (!errors.hasFieldErrors("planId")) {
				Plan plan = planRepository.findByTypeAndCostTypeAndId(
						PlanType.Standard.name(), 
						PlanCostType.Paid.name(), 
						Long.parseLong(signupForm.getPlanId()));
				
				if (plan == null) {
					errors.rejectValue("planId", "invalid", "Plan is invalid");
				}
			}
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to validate your plan.", exception);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public void validateCard(Object target, Errors errors, ModelMap modelMap) throws SignupException {
		SignupForm signupForm = (SignupForm) target;
		List<Card> cards = (List<Card>) modelMap.getOrDefault("cards", new ArrayList<Card>());
		try {
			if (StringUtils.isNotBlank(signupForm.getCardNumber()) && !errors.hasFieldErrors("cardNumber")) {
				Boolean luhnCheck = cardSupport.luhnOnCards(cards, signupForm.getCardNumber());
				if (!luhnCheck) {
					errors.rejectValue("cardNumber", "invalid", "Card number is invalid");					
				}
				else {
					String bin = cardSupport.bin(signupForm.getCardNumber());
					CardBin cardBin = cardBinRepository.findByBin(bin);
					if (cardBin == null) {
						errors.rejectValue("cardNumber", "invalid", "Card number is invalid");
					}
				}
			}
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to validate your credit card.", exception);
		}
	}
	
	public void validateExpiry(Object target, Errors errors) throws SignupException {
		SignupForm signupForm = (SignupForm) target;		
		try {
			String expMth = signupForm.getExpiryMonth();
			String expYr = signupForm.getExpiryYear();
			
			if (StringUtils.isNotBlank(expMth) && !errors.hasFieldErrors("expiryMonth") && 
					StringUtils.isNotBlank(expYr) && !errors.hasFieldErrors("expiryYear")) {
				
				if (cardSupport.expired(expMth, expYr)) {
					errors.rejectValue("expiryMonth", "invalid", "Expiry month is expired");
					errors.rejectValue("expiryYear", "invalid", "Expiry year is expired");
				}		
			}
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to validate your card expiry.", exception);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void validateCvv(Object target, Errors errors, ModelMap modelMap) throws SignupException {
		SignupForm signupForm = (SignupForm) target;
		List<Card> cards = (List<Card>) modelMap.getOrDefault("cards", new ArrayList<Card>());
		
		if (StringUtils.isNotBlank(signupForm.getCardNumber()) && !errors.hasFieldErrors("cardNumber") &&
				StringUtils.isNotBlank(signupForm.getCvv()) && !errors.hasFieldErrors("cvv")) {
			
			Card card = cardSupport.card(cards, signupForm.getCardNumber());
			
			if (card == null) {
				errors.rejectValue("cardNumber", "invalid", "Card number is invalid");
			}
			else {
				if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.AmericanExpress.getBrand())) {
					if (StringUtils.length(signupForm.getCvv()) != 4) {
						errors.rejectValue("cvv", "invalid", "CVV must be 4 digits");
					}
				}
				else {
					if (StringUtils.length(signupForm.getCvv()) != 3) {
						errors.rejectValue("cvv", "invalid", "CVV must be 3 digits");
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = true)
	private void validateIfEmailAddressIsRegistered(SignupForm signupForm, Errors errors) {
		if (!errors.hasFieldErrors("emailAddress")) {
			ClientUser clientUser = clientUserRepository.findByEmailAddress(signupForm.getEmailAddress());
			
			if (clientUser != null) {
				errors.rejectValue("emailAddress", "invalid", "Email address is already registered");
			}
		}
	}
	
	private void validatePassword(SignupForm signupForm, Errors errors) {
		if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")) {
			if (!passwordSupport.isStrong(signupForm.getPassword())) {
				errors.rejectValue("password", "invalid", "Password is not strong enough");
			}
			else {
				if (!StringUtils.equals(signupForm.getPassword(), signupForm.getConfirmPassword())) {
					errors.rejectValue("confirmPassword", "invalid", "Confirm password does not match");
				}
			}			
		}
	}
}