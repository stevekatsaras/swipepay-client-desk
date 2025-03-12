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
import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.CardBin;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCrypto;
import io.swipepay.clientdesk.domain.ClientWallet;
import io.swipepay.clientdesk.domain.ClientWalletCard;
import io.swipepay.clientdesk.domain.enums.CardPattern;
import io.swipepay.clientdesk.domain.enums.CardType;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientWalletCardException;
import io.swipepay.clientdesk.form.ClientWalletCardForm;
import io.swipepay.clientdesk.repository.CardBinRepository;
import io.swipepay.clientdesk.repository.CardRepository;
import io.swipepay.clientdesk.repository.ClientCryptoRepository;
import io.swipepay.clientdesk.repository.ClientWalletCardRepository;
import io.swipepay.clientdesk.repository.ClientWalletRepository;
import io.swipepay.clientdesk.support.CalendarSupport;
import io.swipepay.clientdesk.support.CardSupport;
import io.swipepay.cryptoservicelib.client.CryptoServiceClient;
import io.swipepay.cryptoservicelib.dto.EncryptDataRequest;
import io.swipepay.cryptoservicelib.dto.EncryptDataResponse;

@Service
public class ClientWalletCardService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CalendarSupport calendarSupport;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private CardBinRepository cardBinRepository;
	
	@Autowired
	private ClientCryptoRepository clientCryptoRepository;
	
	@Autowired
	private ClientWalletRepository clientWalletRepository;
	
	@Autowired
	private ClientWalletCardRepository clientWalletCardRepository;
	
	@Autowired
	private CryptoServiceClient cryptoServiceClient;
	
	public void init(ModelMap modelMap) throws ClientWalletCardException {
		try {
			modelMap.addAttribute("cards", cardRepository.findByTypeOrderByBrandAsc(CardType.credit.name()));
			modelMap.addAttribute("months", calendarSupport.listMonthsOfTheYear());
			modelMap.addAttribute("years", calendarSupport.listFutureYearsFromNow(15));
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to get your wallet card metadata.", exception);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void addWalletCard(ClientWalletCardForm clientWalletCardForm, ModelMap modelMap) throws ClientWalletCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientCrypto clientCrypto = clientCryptoRepository.findByClientAndEnabled(client, true); // enabled
			ClientWallet clientWallet = clientWalletRepository.findByClient(client);
			
			if (clientWalletCardForm.getIsDefault()) {
				// if the card we're adding has default selected, then we need to locate 
				// the 'defaulted' card in the database and un-assign it.
				
				ClientWalletCard defaultClientWalletCard = clientWalletCardRepository.findByClientWalletAndIsDefaultTrue(clientWallet);
				defaultClientWalletCard.setIsDefault(false);
				
				clientWalletCardRepository.saveAndFlush(defaultClientWalletCard);
			}
			
			EncryptDataResponse encryptDataResponse = cryptoServiceClient.encrypt(new EncryptDataRequest(
					clientCrypto.getDataKey(), 
					clientWalletCardForm.getCardNumber()));
			
			String bin = cardSupport.bin(clientWalletCardForm.getCardNumber());
			String pan = cardSupport.mask(clientWalletCardForm.getCardNumber(), CardPattern.X.name());
			
			CardBin cardBin = cardBinRepository.findByBin(bin);
			Card card = cardSupport.card((List<Card>) modelMap.get("cards"), clientWalletCardForm.getCardNumber());
			
			ClientWalletCard clientWalletCard = new ClientWalletCard(
					"crd_" + UUID.randomUUID().toString().replaceAll("-", ""),
					clientWalletCardForm.getCardHolderName(), 
					clientWalletCardForm.getCardHolderEmail(), 
					encryptDataResponse.getCipherTextData(), 
					pan, 
					clientWalletCardForm.getExpiryMonth(), 
					clientWalletCardForm.getExpiryYear(), 
					card, 
					cardBin, 
					clientWalletCardForm.getEnabled(),
					clientWalletCardForm.getIsDefault(), 
					LocalDateTime.now(), 
					clientWallet, 
					clientCrypto);
		
			clientWalletCardRepository.saveAndFlush(clientWalletCard);
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to add your wallet card.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void getWalletCard(String code, ClientWalletCardForm clientWalletCardForm) throws ClientWalletCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientWallet clientWallet = clientWalletRepository.findByClient(client);
			ClientWalletCard clientWalletCard = clientWalletCardRepository.findByClientWalletAndCode(clientWallet, code);
			
			if (clientWalletCard == null) {
				String errorMessage = new StringBuilder()
					.append("Expected a client wallet card returned for client ")
					.append("[" + client.getId() + " - " + client.getName() + "] ")
					.append("but NULL was returned. ")
					.append("Aborting getWalletCard().")
					.toString();
								
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientWalletCardForm.setCode(clientWalletCard.getCode());
			clientWalletCardForm.setCardHolderName(clientWalletCard.getCardHolderName());
			clientWalletCardForm.setCardHolderEmail(clientWalletCard.getCardHolderEmail());
			clientWalletCardForm.setCardPan(clientWalletCard.getCardPan());
			clientWalletCardForm.setExpiryMonth(clientWalletCard.getExpiryMonth());
			clientWalletCardForm.setExpiryYear(clientWalletCard.getExpiryYear());
			
			clientWalletCardForm.setIsExpired(cardSupport.expired(
					clientWalletCard.getExpiryMonth(), 
					clientWalletCard.getExpiryYear()));
			
			clientWalletCardForm.setEnabled(clientWalletCard.getEnabled());
			clientWalletCardForm.setIsDefault(clientWalletCard.getIsDefault());
			
			clientWalletCardForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientWalletCard.getModified()));
			
			clientWalletCardForm.setCardIcon(clientWalletCard.getCard().getIcon());
			clientWalletCardForm.setCardBrand(clientWalletCard.getCardBin().getCardBrand());
			clientWalletCardForm.setCardBank(clientWalletCard.getCardBin().getBank());
			clientWalletCardForm.setCardCountry(clientWalletCard.getCardBin().getCountry());
			clientWalletCardForm.setCardLocation(clientWalletCard.getCardBin().getLocation());
			clientWalletCardForm.setCardBankPhone(clientWalletCard.getCardBin().getBankPhone());
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to get your wallet card.", exception);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void editWalletCard(String code, ClientWalletCardForm clientWalletCardForm, ModelMap modelMap) throws ClientWalletCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientWallet clientWallet = clientWalletRepository.findByClient(client);
			ClientWalletCard clientWalletCard = clientWalletCardRepository.findByClientWalletAndCode(clientWallet, code);
			
			if (clientWalletCard == null) {
				String errorMessage = new StringBuilder()
					.append("Expected a client wallet card returned for client ")
					.append("[" + client.getId() + " - " + client.getName() + "] ")
					.append("but NULL was returned. ")
					.append("Aborting getWalletCard().")
					.toString();
								
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			if (clientWalletCard.getIsDefault() && !clientWalletCardForm.getIsDefault()) {
				// if the card we're editing has default un-selected, but is 'default' in the database,
				// then we need to re-assign the 'default' flag to another card in the wallet.
				
				List<ClientWalletCard> nonDefaultClientWalletCards = clientWalletCardRepository.findByClientWalletAndIsDefaultFalse(clientWallet);
				ClientWalletCard nonDefaultClientWalletCard = nonDefaultClientWalletCards.get(0);
				nonDefaultClientWalletCard.setIsDefault(true);
				
				clientWalletCardRepository.saveAndFlush(nonDefaultClientWalletCard);
			}
			else if (clientWalletCardForm.getIsDefault()) {
				// if the card we're editing has default selected, then we need to locate 
				// the 'defaulted' card in the database and un-assign it.
				
				ClientWalletCard defaultClientWalletCard = clientWalletCardRepository.findByClientWalletAndIsDefaultTrue(clientWallet);
				defaultClientWalletCard.setIsDefault(false);
				
				clientWalletCardRepository.saveAndFlush(defaultClientWalletCard);
			}
			
			if (StringUtils.isNotBlank(clientWalletCardForm.getCardNumber())) {
				ClientCrypto clientCrypto = clientCryptoRepository.findByClientAndEnabled(client, true); // enabled
				
				EncryptDataResponse encryptDataResponse = cryptoServiceClient.encrypt(new EncryptDataRequest(
						clientCrypto.getDataKey(), 
						clientWalletCardForm.getCardNumber()));
				
				String bin = cardSupport.bin(clientWalletCardForm.getCardNumber());
				String pan = cardSupport.mask(clientWalletCardForm.getCardNumber(), CardPattern.X.name());
				
				CardBin cardBin = cardBinRepository.findByBin(bin);
				Card card = cardSupport.card((List<Card>) modelMap.get("cards"), clientWalletCardForm.getCardNumber());
				
				clientWalletCard.setCardNumber(encryptDataResponse.getCipherTextData());
				clientWalletCard.setCardPan(pan);
				clientWalletCard.setCard(card);
				clientWalletCard.setCardBin(cardBin);
			}
			clientWalletCard.setCardHolderName(clientWalletCardForm.getCardHolderName());
			clientWalletCard.setCardHolderEmail(clientWalletCardForm.getCardHolderEmail());
			clientWalletCard.setExpiryMonth(clientWalletCardForm.getExpiryMonth());
			clientWalletCard.setExpiryYear(clientWalletCardForm.getExpiryYear());
			clientWalletCard.setEnabled(clientWalletCardForm.getEnabled());
			clientWalletCard.setIsDefault(clientWalletCardForm.getIsDefault());
			clientWalletCard.setModified(LocalDateTime.now());
			
			clientWalletCardRepository.saveAndFlush(clientWalletCard);
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to edit your wallet card.", exception);
		}
	}
}