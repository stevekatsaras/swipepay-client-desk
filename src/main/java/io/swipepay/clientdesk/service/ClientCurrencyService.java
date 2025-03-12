package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCurrency;
import io.swipepay.clientdesk.domain.Currency;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientCurrencyException;
import io.swipepay.clientdesk.form.ClientCurrencyForm;
import io.swipepay.clientdesk.form.wrapper.ClientCurrencyFormWrapper;
import io.swipepay.clientdesk.repository.ClientCurrencyRepository;
import io.swipepay.clientdesk.repository.CurrencyRepository;

@Service
public class ClientCurrencyService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientCurrencyRepository clientCurrencyRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Transactional(readOnly = true)
	public void init(ClientCurrencyFormWrapper clientCurrencyFormWrapper) throws ClientCurrencyException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<Currency> currencies = currencyRepository.findAllByOrderByIso3Asc();
			
			List<ClientCurrencyForm> clientCurrencyForms = new ArrayList<ClientCurrencyForm>();
			for (Currency currency : currencies) {
				ClientCurrency clientCurrency = clientCurrencyRepository.findByClientAndCurrency(client, currency);
				
				ClientCurrencyForm clientCurrencyForm = new ClientCurrencyForm();
				clientCurrencyForm.setEnabled(false);				
				clientCurrencyForm.setCurrencyId(Long.toString(currency.getId()));
				clientCurrencyForm.setCurrencyIso3(currency.getIso3());
				clientCurrencyForm.setCurrencySymbol(currency.getSymbol());
				clientCurrencyForm.setCurrencySymbolIcon(currency.getSymbolIcon());
				
				if (clientCurrency != null) {
					clientCurrencyForm.setCode(clientCurrency.getCode());
					clientCurrencyForm.setEnabled(clientCurrency.getEnabled());
					
					clientCurrencyForm.setModified(DateTimeFormatter.ofPattern(
							DateTimePattern.dMMMyyyyhhmma.toString()).format(
									clientCurrency.getModified()));
				}				
				clientCurrencyForms.add(clientCurrencyForm);
			}
			clientCurrencyFormWrapper.setClientCurrencyForms(clientCurrencyForms);
		}
		catch (Exception exception) {
			throw new ClientCurrencyException("We are unable to get your currencies.", exception);
		}
	}

	@Transactional(readOnly = false)
	public void editCurrencies(ClientCurrencyFormWrapper clientCurrencyFormWrapper) throws ClientCurrencyException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			List<ClientCurrencyForm> clientCurrencyForms = clientCurrencyFormWrapper.getClientCurrencyForms();
			for (ClientCurrencyForm clientCurrencyForm : clientCurrencyForms) {
				Currency currency = currencyRepository.getOne(Long.parseLong(clientCurrencyForm.getCurrencyId()));
				
				if (StringUtils.isBlank(clientCurrencyForm.getCode())) {
					ClientCurrency clientCurrency = new ClientCurrency(
							"cur_" + UUID.randomUUID().toString().replaceAll("-", ""),
							clientCurrencyForm.getEnabled(), 
							LocalDateTime.now(),
							client, 
							currency);
					
					clientCurrencyRepository.saveAndFlush(clientCurrency);
				}
				else {
					ClientCurrency clientCurrency = clientCurrencyRepository.findByClientAndCurrencyAndCode(
							client, 
							currency, 
							clientCurrencyForm.getCode());
					
					if (clientCurrency == null) {
						String errorMessage = new StringBuilder()
								.append("Expected a client currency returned for client ")
								.append("[" + client.getId() + " - " + client.getName() + "] ")
								.append("but NULL was returned. ")
								.append("Aborting editCurrencies().")
								.toString();
											
						throw new EmptyResultDataAccessException(errorMessage, 1);
					}
					
					if (clientCurrency.getEnabled() != clientCurrencyForm.getEnabled()) {
						clientCurrency.setEnabled(clientCurrencyForm.getEnabled());
						clientCurrency.setModified(LocalDateTime.now());
					
						clientCurrencyRepository.saveAndFlush(clientCurrency);
					}
				}
			}
		}
		catch (Exception exception) {
			throw new ClientCurrencyException("We are unable to edit your currencies.", exception);
		}
	}
}