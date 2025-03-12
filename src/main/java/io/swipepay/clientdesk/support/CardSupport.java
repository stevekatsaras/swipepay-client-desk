package io.swipepay.clientdesk.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.CodeValidator;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.springframework.stereotype.Component;

import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.ClientCard;
import io.swipepay.clientdesk.domain.enums.CardBrand;

@Component
public class CardSupport {
	public final Integer CARD_NUMBER_PREFIX_LENGTH = 6; // the first 6 digits of the card (bin)
	public final Integer CARD_NUMBER_SUFFIX_LENGTH = 3; // the last 3 digits of the card
	
	public String bin(String cardNumber) {
		return StringUtils.substring(cardNumber, 0, CARD_NUMBER_PREFIX_LENGTH);
	}
	
	// returns the Card entity based on the card number
	
	public Card card(List<Card> cards, String cardNumber) {
		Card card = null;
		for (int i = 0; i < cards.size(); i++) {
			card = cards.get(i);

			CodeValidator validator = null;
			if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.Visa.getBrand())) {
				validator = CreditCardValidator.VISA_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.MasterCard.getBrand())) {
				validator = CreditCardValidator.MASTERCARD_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.AmericanExpress.getBrand())) {
				validator = CreditCardValidator.AMEX_VALIDATOR;
			}
			if (validator != null) {
				if (validator.isValid(cardNumber)) {
					break;
				}
			}
		}
		return card;
	}
	
	// return the client's card entity based on the card number
	
	public ClientCard clientCard(List<ClientCard> clientCards, String cardNumber) {
		ClientCard clientCard = null;
		for (int i = 0; i < clientCards.size(); i++) {
			clientCard = clientCards.get(i);

			CodeValidator validator = null;
			if (StringUtils.equalsIgnoreCase(clientCard.getCard().getBrand(), CardBrand.Visa.getBrand())) {
				validator = CreditCardValidator.VISA_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(clientCard.getCard().getBrand(), CardBrand.MasterCard.getBrand())) {
				validator = CreditCardValidator.MASTERCARD_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(clientCard.getCard().getBrand(), CardBrand.AmericanExpress.getBrand())) {
				validator = CreditCardValidator.AMEX_VALIDATOR;
			}
			if (validator != null) {
				if (validator.isValid(cardNumber)) {
					break;
				}
			}
		}
		return clientCard;
	}
	
	// returns boolean of whether a card expiry entered is expired
	
	public boolean expired(String expMth, String expYr) throws ParseException {
		Calendar yrCal = Calendar.getInstance();
		yrCal.setTime(new SimpleDateFormat("yy").parse(expYr));
		
		LocalDate expiryDate = LocalDate.of(yrCal.get(Calendar.YEAR), Integer.parseInt(expMth), 1); // default date to first day of the month
		expiryDate = expiryDate.withDayOfMonth(expiryDate.lengthOfMonth()); // alters the date for the last day of the month
		
		return LocalDate.now().isAfter(expiryDate);
	}
	
	// returns a masked pan
	
	public String mask(String cardNumber, String pattern) {
		Integer lengthDiff = cardNumber.length() - CARD_NUMBER_PREFIX_LENGTH - CARD_NUMBER_SUFFIX_LENGTH;
		String maskedPan = StringUtils.overlay(
				cardNumber, 
				StringUtils.repeat(pattern.toString(), lengthDiff), 
				CARD_NUMBER_PREFIX_LENGTH, 
				cardNumber.length() - CARD_NUMBER_SUFFIX_LENGTH);
		
		return maskedPan;
	}
	
	// returns whether a card number passes a luhn check using a list of valid cards
	
	public boolean luhnOnCards(List<Card> cards, String cardNumber) {
		boolean passesLuhn = false;
		for (Card card : cards) {
			CodeValidator validator = null;
			if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.Visa.getBrand())) {
				validator = CreditCardValidator.VISA_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.MasterCard.getBrand())) {
				validator = CreditCardValidator.MASTERCARD_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(card.getBrand(), CardBrand.AmericanExpress.getBrand())) {
				validator = CreditCardValidator.AMEX_VALIDATOR;
			}
			if (validator != null) {
				if (validator.isValid(cardNumber)) {
					passesLuhn = true;
					break;
				}				
			}
		}
		return passesLuhn;
	}
	
	// returns whether a card number passes a luhn check using a list of valid client cards
	
	public boolean luhnOnClientCards(List<ClientCard> clientCards, String cardNumber) {
		boolean passesLuhn = false;
		for (ClientCard clientCard : clientCards) {
			CodeValidator validator = null;
			if (StringUtils.equalsIgnoreCase(clientCard.getCard().getBrand(), CardBrand.Visa.getBrand())) {
				validator = CreditCardValidator.VISA_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(clientCard.getCard().getBrand(), CardBrand.MasterCard.getBrand())) {
				validator = CreditCardValidator.MASTERCARD_VALIDATOR;
			}
			else if (StringUtils.equalsIgnoreCase(clientCard.getCard().getBrand(), CardBrand.AmericanExpress.getBrand())) {
				validator = CreditCardValidator.AMEX_VALIDATOR;
			}
			if (validator != null) {
				if (validator.isValid(cardNumber)) {
					passesLuhn = true;
					break;
				}				
			}
		}
		return passesLuhn;
	}
}