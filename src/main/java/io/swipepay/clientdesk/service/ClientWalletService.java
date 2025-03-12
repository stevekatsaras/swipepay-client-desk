package io.swipepay.clientdesk.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientWallet;
import io.swipepay.clientdesk.domain.ClientWalletCard;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientWalletCardException;
import io.swipepay.clientdesk.form.ClientWalletCardForm;
import io.swipepay.clientdesk.repository.ClientWalletCardRepository;
import io.swipepay.clientdesk.repository.ClientWalletRepository;
import io.swipepay.clientdesk.support.CardSupport;

@Service
public class ClientWalletService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientWalletRepository clientWalletRepository;
	
	@Autowired
	private ClientWalletCardRepository clientWalletCardRepository;
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientWalletCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientWallet clientWallet = clientWalletRepository.findByClient(client);
			List<ClientWalletCard> clientWalletCards = clientWalletCardRepository.findByClientWalletOrderByIsDefaultDesc(clientWallet);
			
			List<ClientWalletCardForm> walletCards = new ArrayList<ClientWalletCardForm>();
			for (ClientWalletCard clientWalletCard: clientWalletCards) {
				ClientWalletCardForm clientWalletCardForm = new ClientWalletCardForm();
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
				walletCards.add(clientWalletCardForm);
			}
			modelMap.addAttribute("walletCards", walletCards);
		}
		catch (Exception exception) {
			throw new ClientWalletCardException("We are unable to get your wallet metadata.", exception);
		}
	}
}