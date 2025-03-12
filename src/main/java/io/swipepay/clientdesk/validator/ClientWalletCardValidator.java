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

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.CardBin;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientWallet;
import io.swipepay.clientdesk.exception.ClientWalletCardException;
import io.swipepay.clientdesk.form.ClientWalletCardForm;
import io.swipepay.clientdesk.repository.CardBinRepository;
import io.swipepay.clientdesk.repository.ClientWalletCardRepository;
import io.swipepay.clientdesk.repository.ClientWalletRepository;
import io.swipepay.clientdesk.support.CardSupport;

@Component
public class ClientWalletCardValidator implements Validator {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CardBinRepository cardBinRepository;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientWalletRepository clientWalletRepository;
	
	@Autowired
	private ClientWalletCardRepository clientWalletCardRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return ClientWalletCardForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public void validateCard(Object target, Errors errors, ModelMap modelMap) throws ClientWalletCardException {
		ClientWalletCardForm clientWalletCardForm = (ClientWalletCardForm) target;
		List<Card> cards = (List<Card>) modelMap.getOrDefault("cards", new ArrayList<Card>());
		try {
			if (StringUtils.isNotBlank(clientWalletCardForm.getCardNumber()) && !errors.hasFieldErrors("cardNumber")) {
				Boolean luhnCheck = cardSupport.luhnOnCards(cards, clientWalletCardForm.getCardNumber());
				if (!luhnCheck) {
					errors.rejectValue("cardNumber", "invalid", "Card number is invalid");			
				}
				else {
					String bin = cardSupport.bin(clientWalletCardForm.getCardNumber());
					CardBin cardBin = cardBinRepository.findByBin(bin);
					if (cardBin == null) {
						errors.rejectValue("cardNumber", "invalid", "Card number is invalid");
					}
				}
			}
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to validate your credit card.", exception);
		}
	}
	
	public void validateExpiry(Object target, Errors errors) throws ClientWalletCardException {
		ClientWalletCardForm clientWalletCardForm = (ClientWalletCardForm) target;
		try {
			String expMth = clientWalletCardForm.getExpiryMonth();
			String expYr = clientWalletCardForm.getExpiryYear();
			
			if (StringUtils.isNotBlank(expMth) && !errors.hasFieldErrors("expiryMonth") && 
					StringUtils.isNotBlank(expYr) && !errors.hasFieldErrors("expiryYear")) {
				
				if (cardSupport.expired(expMth, expYr)) {
					errors.rejectValue("expiryMonth", "invalid", "Expiry month is expired");
					errors.rejectValue("expiryYear", "invalid", "Expiry year is expired");
				}		
			}
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to validate your card expiry.", exception);
		}
	}	

	public void validateIsDefaultAllowedOnDisabledCard(Object target, Errors errors) {
		ClientWalletCardForm clientWalletCardForm = (ClientWalletCardForm) target;
		
		if (!clientWalletCardForm.getEnabled() && clientWalletCardForm.getIsDefault()) {
			errors.rejectValue("isDefault", "invalid", "Cannot set to default a disabled card");		
		}
	}
	
	@Transactional(readOnly = true)
	public void validateIsNoDefaultAllowedOnSingleCard(Object target, Errors errors) throws ClientWalletCardException {
		ClientWalletCardForm clientWalletCardForm = (ClientWalletCardForm) target;
		
		if (!clientWalletCardForm.getIsDefault()) {
			try {
				Client client = authenticationFacade.getClientFromPrincipal();
				ClientWallet clientWallet = clientWalletRepository.findByClient(client);
				Long countWalletCards = clientWalletCardRepository.countByClientWallet(clientWallet);
				
				if (countWalletCards == 1) {
					errors.rejectValue("isDefault", "invalid", "Cannot remove default from a single card");
				}
			}
			catch (Exception exception) {
				throw new ClientWalletCardException("We are unable to validate a non-default card.", exception);
			}
		}
	}
}