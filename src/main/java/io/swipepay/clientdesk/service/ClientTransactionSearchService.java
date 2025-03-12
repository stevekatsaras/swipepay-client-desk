package io.swipepay.clientdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.enums.ClientTransactionEvent;
import io.swipepay.clientdesk.domain.enums.ClientTransactionStatus;
import io.swipepay.clientdesk.domain.enums.SearchAmountCriteria;
import io.swipepay.clientdesk.exception.ClientTransactionSearchException;
import io.swipepay.clientdesk.form.ClientTransactionSearchForm;
import io.swipepay.clientdesk.repository.ClientCardRepository;
import io.swipepay.clientdesk.repository.ClientCurrencyRepository;
import io.swipepay.clientdesk.repository.ClientProfileRepository;
import io.swipepay.clientdesk.repository.TransactionResponseRepository;
import io.swipepay.clientdesk.repository.TransactionTypeRepository;
import io.swipepay.clientdesk.support.CalendarSupport;

@Service
public class ClientTransactionSearchService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CalendarSupport calendarSupport;
	
	@Autowired
	private ClientCardRepository clientCardRepository;
	
	@Autowired
	private ClientCurrencyRepository clientCurrencyRepository;
	
	@Autowired
	private ClientProfileRepository clientProfileRepository;
	
	@Autowired
	private TransactionResponseRepository transactionResponseRepository;
	
	@Autowired
	private TransactionTypeRepository transactionTypeRepository;
	
	@Transactional(readOnly = true)
	public void init(ClientTransactionSearchForm clientTransactionSearchForm, ModelMap modelMap) throws ClientTransactionSearchException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			modelMap.addAttribute("clientProfiles", clientProfileRepository.findByClient(client));
			modelMap.addAttribute("transactionTypes", transactionTypeRepository.findAll());
			modelMap.addAttribute("amountCriterias", SearchAmountCriteria.values());
			modelMap.addAttribute("clientCurrencies", clientCurrencyRepository.findByClient(client));

			modelMap.addAttribute("months", calendarSupport.listMonthsOfTheYear());
			
//			modelMap.addAttribute("clientCards", clientCardRepository.findByClientAndEnabled(client, true)); // enabled
//			modelMap.addAttribute("statuses", ClientTransactionStatus.values());
//			modelMap.addAttribute("events", ClientTransactionEvent.values());
//			modelMap.addAttribute("transactionResponses", transactionResponseRepository.findAll());
		}
		catch (Exception exception) {
			throw new ClientTransactionSearchException("We are unable to get your transaction metadata.", exception);
		}
	}

}