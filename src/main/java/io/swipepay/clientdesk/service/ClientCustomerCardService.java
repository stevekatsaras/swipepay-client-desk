package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.CardBin;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCard;
import io.swipepay.clientdesk.domain.ClientCrypto;
import io.swipepay.clientdesk.domain.ClientCustomer;
import io.swipepay.clientdesk.domain.ClientCustomerCard;
import io.swipepay.clientdesk.domain.enums.CardPattern;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientCustomerCardException;
import io.swipepay.clientdesk.form.ClientCustomerCardForm;
import io.swipepay.clientdesk.repository.CardBinRepository;
import io.swipepay.clientdesk.repository.ClientCardRepository;
import io.swipepay.clientdesk.repository.ClientCryptoRepository;
import io.swipepay.clientdesk.repository.ClientCustomerCardRepository;
import io.swipepay.clientdesk.repository.ClientCustomerRepository;
import io.swipepay.clientdesk.support.CalendarSupport;
import io.swipepay.clientdesk.support.CardSupport;
import io.swipepay.cryptoservicelib.client.CryptoServiceClient;
import io.swipepay.cryptoservicelib.dto.EncryptDataRequest;
import io.swipepay.cryptoservicelib.dto.EncryptDataResponse;

@Service
public class ClientCustomerCardService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CalendarSupport calendarSupport;
	
	@Autowired
	private CardBinRepository cardBinRepository;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientCardRepository clientCardRepository;
	
	@Autowired
	private ClientCustomerRepository clientCustomerRepository;
	
	@Autowired
	private ClientCustomerCardRepository clientCustomerCardRepository;
	
	@Autowired
	private ClientCryptoRepository clientCryptoRepository;
	
	@Autowired
	private CryptoServiceClient cryptoServiceClient;
	
	@Transactional(readOnly = true)
	public void init(String customerCode, ModelMap modelMap) throws ClientCustomerCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, customerCode);
			
			if (clientCustomer == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting init().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			modelMap.addAttribute("clientCards", clientCardRepository.findByClientAndEnabled(client, true)); // enabled
			modelMap.addAttribute("months", calendarSupport.listMonthsOfTheYear());
			modelMap.addAttribute("years", calendarSupport.listFutureYearsFromNow(15));
		}
		catch (Exception exception) {
			throw new ClientCustomerCardException("We are unable to get your customer card metadata.", exception);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void addCustomerCard(
			String customerCode, 
			ClientCustomerCardForm clientCustomerCardForm, 
			ModelMap modelMap) throws ClientCustomerCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCrypto clientCrypto = clientCryptoRepository.findByClientAndEnabled(client, true); // enabled
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, customerCode);
			
			if (clientCustomer == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting addCustomerCard().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			Long countCustomerCards = clientCustomerCardRepository.countByClientCustomer(clientCustomer);
			
			if (countCustomerCards == 0) {
				clientCustomerCardForm.setIsDefault(true);
			}
			else if (clientCustomerCardForm.getIsDefault()) {
				// if the card we're adding has default selected, then we need to locate 
				// the 'defaulted' card in the database and un-assign it.
				
				ClientCustomerCard defaultClientCustomerCard = clientCustomerCardRepository.findByClientCustomerAndIsDefaultTrue(clientCustomer);
				defaultClientCustomerCard.setIsDefault(false);
				
				clientCustomerCardRepository.saveAndFlush(defaultClientCustomerCard);
			}
			
			EncryptDataResponse encryptDataResponse = cryptoServiceClient.encrypt(new EncryptDataRequest(
					clientCrypto.getDataKey(), 
					clientCustomerCardForm.getCardNumber()));
			
			String bin = cardSupport.bin(clientCustomerCardForm.getCardNumber());
			String pan = cardSupport.mask(clientCustomerCardForm.getCardNumber(), CardPattern.X.name());
			
			CardBin cardBin = cardBinRepository.findByBin(bin);
			ClientCard clientCard = cardSupport.clientCard((List<ClientCard>) modelMap.get("clientCards"), clientCustomerCardForm.getCardNumber());
			
			ClientCustomerCard clientCustomerCard = new ClientCustomerCard(
					"crd_" + UUID.randomUUID().toString().replaceAll("-", ""),
					clientCustomerCardForm.getCardHolderName(), 
					clientCustomerCardForm.getCardHolderEmail(), 
					encryptDataResponse.getCipherTextData(), 
					pan, 
					clientCustomerCardForm.getExpiryMonth(), 
					clientCustomerCardForm.getExpiryYear(), 
					clientCard, 
					cardBin, 
					clientCustomerCardForm.getEnabled(), 
					clientCustomerCardForm.getIsDefault(), 
					LocalDateTime.now(), 
					clientCustomer, 
					clientCrypto);
			
			clientCustomerCardRepository.saveAndFlush(clientCustomerCard);
		}
		catch (Exception exception) {
			throw new ClientCustomerCardException("We are unable to add your customer card.", exception);
		}
	}

	@Transactional(readOnly = true)
	public void getCustomerCard(
			String customerCode, 
			String code, 
			ClientCustomerCardForm clientCustomerCardForm) throws ClientCustomerCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, customerCode);
			ClientCustomerCard clientCustomerCard = clientCustomerCardRepository.findByClientCustomerAndCode(clientCustomer, code);
			
			if (clientCustomer == null && clientCustomerCard == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer & client customer card returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting getCustomerCard().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
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
			clientCustomerCardForm.setCardBank(clientCustomerCard.getCardBin().getBank());
			clientCustomerCardForm.setCardCountry(clientCustomerCard.getCardBin().getCountry());
			clientCustomerCardForm.setCardLocation(clientCustomerCard.getCardBin().getLocation());
			clientCustomerCardForm.setCardBankPhone(clientCustomerCard.getCardBin().getBankPhone());
			clientCustomerCardForm.setCardIcon(clientCustomerCard.getClientCard().getCard().getIcon());
			
			clientCustomerCardForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientCustomerCard.getModified()));
		}
		catch (Exception exception) {
			throw new ClientCustomerCardException("We are unable to get your customer card.", exception);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void editCustomerCard(
			String customerCode, 
			String code, 
			ClientCustomerCardForm clientCustomerCardForm, 
			ModelMap modelMap) throws ClientCustomerCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCustomer clientCustomer = clientCustomerRepository.findByClientAndCode(client, customerCode);
			ClientCustomerCard clientCustomerCard = clientCustomerCardRepository.findByClientCustomerAndCode(clientCustomer, code);
			
			if (clientCustomer == null && clientCustomerCard == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client customer & client customer card returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting editCustomerCard().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			if (clientCustomerCard.getIsDefault() && !clientCustomerCardForm.getIsDefault()) {
				// if the card we're editing has default un-selected, but is 'default' in the database,
				// then we need to re-assign the 'default' flag to another card in the wallet.
				
				List<ClientCustomerCard> nonDefaultClientCustomerCards = clientCustomerCardRepository.findByClientCustomerAndIsDefaultFalse(clientCustomer);
				ClientCustomerCard nonDefaultClientCustomerCard = nonDefaultClientCustomerCards.get(0);
				nonDefaultClientCustomerCard.setIsDefault(true);
				
				clientCustomerCardRepository.saveAndFlush(nonDefaultClientCustomerCard);
			}
			else if (clientCustomerCardForm.getIsDefault()) {
				// if the card we're editing has default selected, then we need to locate 
				// the 'defaulted' card in the database and un-assign it.
				
				ClientCustomerCard defaultClientCustomerCard = clientCustomerCardRepository.findByClientCustomerAndIsDefaultTrue(clientCustomer);
				defaultClientCustomerCard.setIsDefault(false);
				
				clientCustomerCardRepository.saveAndFlush(defaultClientCustomerCard);
			}
			
			if (StringUtils.isNotBlank(clientCustomerCardForm.getCardNumber())) {
				ClientCrypto clientCrypto = clientCryptoRepository.findByClientAndEnabled(client, true); // enabled
				
				EncryptDataResponse encryptDataResponse = cryptoServiceClient.encrypt(new EncryptDataRequest(
						clientCrypto.getDataKey(), 
						clientCustomerCardForm.getCardNumber()));
				
				String bin = cardSupport.bin(clientCustomerCardForm.getCardNumber());
				String pan = cardSupport.mask(clientCustomerCardForm.getCardNumber(), CardPattern.X.name());
				
				CardBin cardBin = cardBinRepository.findByBin(bin);
				ClientCard clientCard = cardSupport.clientCard((List<ClientCard>) modelMap.get("clientCards"), clientCustomerCardForm.getCardNumber());
				
				clientCustomerCard.setCardNumber(encryptDataResponse.getCipherTextData());
				clientCustomerCard.setCardPan(pan);
				clientCustomerCard.setClientCard(clientCard);
				clientCustomerCard.setCardBin(cardBin);
			}
			clientCustomerCard.setCardHolderName(clientCustomerCardForm.getCardHolderName());
			clientCustomerCard.setCardHolderEmail(clientCustomerCardForm.getCardHolderEmail());
			clientCustomerCard.setExpiryMonth(clientCustomerCardForm.getExpiryMonth());
			clientCustomerCard.setExpiryYear(clientCustomerCardForm.getExpiryYear());
			clientCustomerCard.setEnabled(clientCustomerCardForm.getEnabled());
			clientCustomerCard.setIsDefault(clientCustomerCardForm.getIsDefault());
			clientCustomerCard.setModified(LocalDateTime.now());
			
			clientCustomerCardRepository.saveAndFlush(clientCustomerCard);
		}
		catch (Exception exception) {
			throw new ClientCustomerCardException("We are unable to edit your customer card.", exception);
		}
	}
}