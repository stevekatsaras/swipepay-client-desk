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
import io.swipepay.clientdesk.domain.ClientCustomer;
import io.swipepay.clientdesk.domain.ClientCustomerCard;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientCustomerException;
import io.swipepay.clientdesk.form.ClientCustomerCardForm;
import io.swipepay.clientdesk.form.ClientCustomerForm;
import io.swipepay.clientdesk.repository.ClientCustomerCardRepository;
import io.swipepay.clientdesk.repository.ClientCustomerRepository;
import io.swipepay.clientdesk.support.CardSupport;

@Service
public class ClientCustomerService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientCustomerRepository clientCustomerRepository;
	
	@Autowired
	private ClientCustomerCardRepository clientCustomerCardRepository;
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientCustomerException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<ClientCustomer> clientCustomers = clientCustomerRepository.findByClient(client);
			
			List<ClientCustomerForm> customers = new ArrayList<ClientCustomerForm>();
			for (ClientCustomer clientCustomer : clientCustomers) {
				ClientCustomerForm clientCustomerForm = new ClientCustomerForm();
				clientCustomerForm.setCode(clientCustomer.getCode());
				clientCustomerForm.setName(clientCustomer.getName());
				clientCustomerForm.setEmailAddress(clientCustomer.getEmail());
				clientCustomerForm.setEnabled(clientCustomer.getEnabled());
				
				clientCustomerForm.setModified(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyyhhmma.toString()).format(
								clientCustomer.getModified()));
				
				customers.add(clientCustomerForm);
			}
			modelMap.addAttribute("customers", customers);
		}
		catch (Exception exception) {
			throw new ClientCustomerException("We are unable to get your customers.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void initCustomer(String code, ModelMap modelMap) throws ClientCustomerException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, code);
			
			if (clientCustomer == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting initCustomer().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			List<ClientCustomerCard> clientCustomerCards = clientCustomerCardRepository.findByClientCustomerOrderByIsDefaultDesc(
					clientCustomer);
			
			List<ClientCustomerCardForm> customerCards = new ArrayList<ClientCustomerCardForm>();
			for (ClientCustomerCard clientCustomerCard: clientCustomerCards) {
				ClientCustomerCardForm clientCustomerCardForm = new ClientCustomerCardForm();
				clientCustomerCardForm.setCode(clientCustomerCard.getCode());
				clientCustomerCardForm.setCardHolderName(clientCustomerCard.getCardHolderName());
				clientCustomerCardForm.setCardHolderEmail(clientCustomerCard.getCardHolderEmail());
				clientCustomerCardForm.setCardPan(clientCustomerCard.getCardPan());
				clientCustomerCardForm.setExpiryMonth(clientCustomerCard.getExpiryMonth());
				clientCustomerCardForm.setExpiryYear(clientCustomerCard.getExpiryYear());

				clientCustomerCardForm.setIsExpired(cardSupport.expired(
						clientCustomerCard.getExpiryMonth(), 
						clientCustomerCard.getExpiryYear()));
				
				clientCustomerCardForm.setEnabled(clientCustomerCard.getEnabled());
				clientCustomerCardForm.setIsDefault(clientCustomerCard.getIsDefault());
				clientCustomerCardForm.setCardBrand(clientCustomerCard.getCardBin().getCardBrand());
				clientCustomerCardForm.setCardIcon(clientCustomerCard.getClientCard().getCard().getIcon());
				
				clientCustomerCardForm.setModified(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyyhhmma.toString()).format(
								clientCustomerCard.getModified()));
				
				customerCards.add(clientCustomerCardForm);
			}
			modelMap.addAttribute("customerCards", customerCards);
		}
		catch (Exception exception) {
			throw new ClientCustomerException("We are unable to get your customer metadata.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void addCustomer(ClientCustomerForm clientCustomerForm) throws ClientCustomerException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			ClientCustomer clientCustomer = new ClientCustomer(
					"cus_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					clientCustomerForm.getName(), 
					clientCustomerForm.getEmailAddress(), 
					clientCustomerForm.getEnabled(), 
					LocalDateTime.now(), 
					client);
			
			clientCustomerRepository.saveAndFlush(clientCustomer);
		}
		catch (Exception exception) {
			throw new ClientCustomerException("We are unable to add your customer.", exception);
		}
	}

	@Transactional(readOnly = true)
	public void getCustomer(String code, ClientCustomerForm clientCustomerForm) throws ClientCustomerException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, code);
			
			if (clientCustomer == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting getCustomer().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientCustomerForm.setCode(clientCustomer.getCode());
			clientCustomerForm.setName(clientCustomer.getName());
			clientCustomerForm.setEmailAddress(clientCustomer.getEmail());
			clientCustomerForm.setEnabled(clientCustomer.getEnabled());
			
			clientCustomerForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientCustomer.getModified()));
		}
		catch (Exception exception) {
			throw new ClientCustomerException("We are unable to get your customer.", exception);
		}
	}

	@Transactional(readOnly = false)
	public void editCustomer(String code, ClientCustomerForm clientCustomerForm) throws ClientCustomerException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, code);
			
			if (clientCustomer == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting editCustomer().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientCustomer.setName(clientCustomerForm.getName());
			clientCustomer.setEmail(clientCustomerForm.getEmailAddress());
			clientCustomer.setEnabled(clientCustomerForm.getEnabled());
			clientCustomer.setModified(LocalDateTime.now());
			
			clientCustomerRepository.saveAndFlush(clientCustomer);
		}
		catch (Exception exception) {
			throw new ClientCustomerException("We are unable to edit your customer.", exception);
		}
	}
}