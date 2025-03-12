package io.swipepay.clientdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.exception.ClientTransactionPaymentException;
import io.swipepay.clientdesk.form.ClientTransactionPaymentForm;
import io.swipepay.clientdesk.repository.ClientCurrencyRepository;
import io.swipepay.clientdesk.repository.ClientProfileRepository;
import io.swipepay.clientdesk.repository.TransactionTypeRepository;
import io.swipepay.clientdesk.support.CalendarSupport;

@Service
public class ClientTransactionPaymentService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CalendarSupport calendarSupport;
	
	@Autowired
	private ClientCurrencyRepository clientCurrencyRepository;
	
	@Autowired
	private ClientProfileRepository clientProfileRepository;
	
	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Transactional(readOnly = true)
	public void init(ClientTransactionPaymentForm clientTransactionPaymentForm, ModelMap modelMap) throws ClientTransactionPaymentException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			modelMap.addAttribute("clientProfiles", clientProfileRepository.findByClientAndEnabled(client, true)); // enabled
			modelMap.addAttribute("transactionTypes", transactionTypeRepository.findByNewTxn(true)); // new transaction types only
			modelMap.addAttribute("clientCurrencies", clientCurrencyRepository.findByClientAndEnabled(client, true)); // enabled
			modelMap.addAttribute("months", calendarSupport.listMonthsOfTheYear());
			modelMap.addAttribute("years", calendarSupport.listFutureYearsFromNow(15));			

//			modelMap.addAttribute("clientCards", clientCardRepository.findByClientAndEnabled(client, true)); // enabled

		}
		catch (Exception exception) {
			throw new ClientTransactionPaymentException("We are unable to get your payment metadata.", exception);
		}
	}

}