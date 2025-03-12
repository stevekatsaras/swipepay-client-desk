package io.swipepay.clientdesk.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.CardBin;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCard;
import io.swipepay.clientdesk.domain.ClientCustomer;
import io.swipepay.clientdesk.exception.ClientCustomerCardException;
import io.swipepay.clientdesk.form.ClientCustomerCardForm;
import io.swipepay.clientdesk.repository.CardBinRepository;
import io.swipepay.clientdesk.repository.ClientCustomerCardRepository;
import io.swipepay.clientdesk.repository.ClientCustomerRepository;
import io.swipepay.clientdesk.support.CardSupport;

@Component
public class ClientCustomerCardValidator implements Validator {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CardBinRepository cardBinRepository;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientCustomerRepository clientCustomerRepository;
	
	@Autowired
	private ClientCustomerCardRepository clientCustomerCardRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return ClientCustomerCardForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public void validateClientCard(Object target, Errors errors, ModelMap modelMap) throws ClientCustomerCardException {
		ClientCustomerCardForm clientCustomerCardForm = (ClientCustomerCardForm) target;
		List<ClientCard> clientCards = (List<ClientCard>) modelMap.getOrDefault("clientCards", new ArrayList<Card>());
		try {
			if (StringUtils.isNotBlank(clientCustomerCardForm.getCardNumber()) && !errors.hasFieldErrors("cardNumber")) {
				Boolean luhnCheck = cardSupport.luhnOnClientCards(clientCards, clientCustomerCardForm.getCardNumber());
				if (!luhnCheck) {
					errors.rejectValue("cardNumber", "invalid", "Card number is invalid");					
				}
				else {
					String bin = cardSupport.bin(clientCustomerCardForm.getCardNumber());
					CardBin cardBin = cardBinRepository.findByBin(bin);
					if (cardBin == null) {
						errors.rejectValue("cardNumber", "invalid", "Card number is invalid");					
					}
				}
			}
		}
		catch (Exception exception) {
			throw new ClientCustomerCardException("We are unable to validate your credit card.", exception);
		}
	}
	
	public void validateExpiry(Object target, Errors errors) throws ClientCustomerCardException {
		ClientCustomerCardForm clientCustomerCardForm = (ClientCustomerCardForm) target;
		try {
			String expMth = clientCustomerCardForm.getExpiryMonth();
			String expYr = clientCustomerCardForm.getExpiryYear();
			
			if (StringUtils.isNotBlank(expMth) && !errors.hasFieldErrors("expiryMonth") && 
					StringUtils.isNotBlank(expYr) && !errors.hasFieldErrors("expiryYear")) {
				
				if (cardSupport.expired(expMth, expYr)) {
					errors.rejectValue("expiryMonth", "invalid", "Expiry month is expired");
					errors.rejectValue("expiryYear", "invalid", "Expiry year is expired");
				}		
			}
		}
		catch (Exception exception) {
			throw new ClientCustomerCardException("We are unable to validate your card expiry.", exception);
		}
	}	

	public void validateIsDefaultAllowedOnDisabledCard(Object target, Errors errors) {
		ClientCustomerCardForm clientCustomerCardForm = (ClientCustomerCardForm) target;
		
		if (!clientCustomerCardForm.getEnabled() && clientCustomerCardForm.getIsDefault()) {
			errors.rejectValue("isDefault", "invalid", "Cannot set to default a disabled card");		
		}
	}
	
	@Transactional(readOnly = true)
	public void validateIsNoDefaultAllowedOnSingleCard(
			String customerCode, 
			Object target, 
			Errors errors) throws ClientCustomerCardException {
		
		ClientCustomerCardForm clientCustomerCardForm = (ClientCustomerCardForm) target;
		
		if (!clientCustomerCardForm.getIsDefault()) {
			try {
				Client client = authenticationFacade.getClientFromPrincipal();
				ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, customerCode);
				
				if (clientCustomer == null) {
					String errorMessage = new StringBuilder()
							.append("Expected a client customer returned for client ")
							.append("[" + client.getId() + " - " + client.getName() + "] ")
							.append("but NULL was returned. ")
							.append("Aborting validateIsNoDefaultAllowedOnSingleCard().")
							.toString();
										
					throw new EmptyResultDataAccessException(errorMessage, 1);
				}
				
				Long countCustomerCards = clientCustomerCardRepository.countByClientCustomer(clientCustomer);
				
				if (countCustomerCards == 1) {
					errors.rejectValue("isDefault", "invalid", "Cannot remove default from a single card");
				}
			}
			catch (Exception exception) {
				throw new ClientCustomerCardException("We are unable to validate a non-default card.", exception);
			}
		}
	}
}