package io.swipepay.clientdesk.support;

import java.text.NumberFormat;
import java.util.Currency;

import org.springframework.stereotype.Component;

@Component
public class NumberSupport {
	
	/**
	 * Formats a money amount, from whole cents into a decimal figure.
	 * @param amount - the amount, in whole cents
	 * @param currencyIso3 - the 3 letter Iso code
	 * @param currencySymbol - the currency symbol
	 * @param currencyDivisible - the unit to divide the amount by
	 * @return a formatted money amount
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public String formatMoney(Long amount, String currencyIso3, Integer currencyDivisible) 
			throws NullPointerException, IllegalArgumentException {
		
		Currency currency = Currency.getInstance(currencyIso3);
		
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		currencyFormat.setCurrency(currency);
		currencyFormat.setMaximumFractionDigits(currency.getDefaultFractionDigits());
		
		String formattedAmount = "";
		switch (currencyDivisible) {
			case 0: {
				formattedAmount = currencyFormat.format(amount);
				break;
			}
			case 100: {
				formattedAmount = currencyFormat.format(amount / 100.0);
				break;
			}
			default: {
				throw new IllegalArgumentException("currencyDivisible argument [" + currencyDivisible + "] is invalid.");
			}
		}
		return formattedAmount;
	}
}
