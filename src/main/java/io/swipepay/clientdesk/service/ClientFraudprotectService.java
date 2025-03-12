package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientFraudprotect;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientFraudprotectException;
import io.swipepay.clientdesk.form.ClientFraudprotectForm;
import io.swipepay.clientdesk.repository.ClientFraudprotectRepository;

@Service
public class ClientFraudprotectService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientFraudprotectRepository clientFraudprotectRepository;
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientFraudprotectException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<ClientFraudprotect> clientFraudRules = clientFraudprotectRepository.findByClient(client);
			
			List<ClientFraudprotectForm> fraudRules = new ArrayList<ClientFraudprotectForm>();
			for (ClientFraudprotect clientFraudRule : clientFraudRules) {
				ClientFraudprotectForm clientFraudprotectForm = new ClientFraudprotectForm();
				clientFraudprotectForm.setCode(clientFraudRule.getCode());
				clientFraudprotectForm.setName(clientFraudRule.getName());
				
				clientFraudprotectForm.setModified(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyyhhmma.toString()).format(
								clientFraudRule.getModified()));
				
				fraudRules.add(clientFraudprotectForm);
			}
			modelMap.addAttribute("fraudRules", fraudRules);
		}
		catch (Exception exception) {
			throw new ClientFraudprotectException("We are unable to get your fraudprotect rules.", exception);
		}
	}
	
	public void initFraudprotect(ModelMap modelMap) throws ClientFraudprotectException {
		try {
			
		}
		catch (Exception exception) {
			throw new ClientFraudprotectException("We are unable to get the fraud metadata.", exception);
		}
	}

	@Transactional(readOnly = false)
	public void addFraudprotect(ClientFraudprotectForm clientFraudprotectForm) throws ClientFraudprotectException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			ClientFraudprotect clientFraudprotect = new ClientFraudprotect(
					"frd_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					clientFraudprotectForm.getName(), 
					LocalDateTime.now(), 
					client);
			
			clientFraudprotectRepository.saveAndFlush(clientFraudprotect);
		}
		catch (Exception exception) {
			throw new ClientFraudprotectException("We are unable to add your fraudprotect rule.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void getFraudprotect(String code, ClientFraudprotectForm clientFraudprotectForm) throws ClientFraudprotectException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientFraudprotect clientFraudprotect = clientFraudprotectRepository.findByClientAndCode(client, code);
			
			if (clientFraudprotect == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client fraudprotect rule returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting getFraudprotect().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}

			clientFraudprotectForm.setCode(clientFraudprotect.getCode());
			clientFraudprotectForm.setName(clientFraudprotect.getName());
			
			clientFraudprotectForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientFraudprotect.getModified()));
		}
		catch (Exception exception) {
			throw new ClientFraudprotectException("We are unable to get your fraudprotect rule.", exception);
		}
	}

	@Transactional(readOnly = false)
	public void editFraudprotect(String code, ClientFraudprotectForm clientFraudprotectForm) throws ClientFraudprotectException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientFraudprotect clientFraudprotect = clientFraudprotectRepository.findByClientAndCode(client, code);
			
			if (clientFraudprotect == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client Fraudprotect rule returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting editFraudprotect().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientFraudprotect.setName(clientFraudprotectForm.getName());
			clientFraudprotect.setModified(LocalDateTime.now());
			
			clientFraudprotectRepository.saveAndFlush(clientFraudprotect);
		}
		catch (Exception exception) {
			throw new ClientFraudprotectException("We are unable to edit your fraudprotect rule.", exception);
		}
	}
}