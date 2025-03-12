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
import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCard;
import io.swipepay.clientdesk.domain.enums.CardType;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientCardException;
import io.swipepay.clientdesk.form.ClientCardForm;
import io.swipepay.clientdesk.form.wrapper.ClientCardFormWrapper;
import io.swipepay.clientdesk.repository.CardRepository;
import io.swipepay.clientdesk.repository.ClientCardRepository;

@Service
public class ClientCardService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientCardRepository clientCardRepository;
	
	@Autowired
	private CardRepository cardRepository;

	@Transactional(readOnly = true)
	public void init(ClientCardFormWrapper clientCardFormWrapper) throws ClientCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<Card> cards = cardRepository.findByTypeOrderByBrandAsc(CardType.credit.name());
			
			List<ClientCardForm> clientCardForms = new ArrayList<ClientCardForm>();
			for (Card card : cards) {
				ClientCard clientCard = clientCardRepository.findByClientAndCard(client, card);
				
				ClientCardForm clientCardForm = new ClientCardForm();
				clientCardForm.setEnabled(false);
				clientCardForm.setCardId(Long.toString(card.getId()));
				clientCardForm.setCardBrand(card.getBrand());
				clientCardForm.setCardAcronym(card.getAcronym());
				clientCardForm.setCardIcon(card.getIcon());
				clientCardForm.setCardType(card.getType());
				
				if (clientCard != null) {
					clientCardForm.setCode(clientCard.getCode());
					clientCardForm.setEnabled(clientCard.getEnabled());
					
					clientCardForm.setModified(DateTimeFormatter.ofPattern(
							DateTimePattern.dMMMyyyyhhmma.toString()).format(
									clientCard.getModified()));
				}
				clientCardForms.add(clientCardForm);
			}
			clientCardFormWrapper.setClientCardForms(clientCardForms);
		}
		catch (Exception exception) {
			throw new ClientCardException("We are unable to get your cards.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void editCards(ClientCardFormWrapper clientCardFormWrapper) throws ClientCardException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			List<ClientCardForm> clientCardForms = clientCardFormWrapper.getClientCardForms();
			for (ClientCardForm clientCardForm : clientCardForms) {
				Card card = cardRepository.getOne(Long.parseLong(clientCardForm.getCardId()));
				
				if (StringUtils.isBlank(clientCardForm.getCode())) {
					ClientCard clientCard = new ClientCard(
							"crd_" + UUID.randomUUID().toString().replaceAll("-", ""),
							clientCardForm.getEnabled(), 
							LocalDateTime.now(),
							client, 
							card);
					
					clientCardRepository.saveAndFlush(clientCard);
				}
				else {
					ClientCard clientCard = clientCardRepository.findByClientAndCardAndCode(
							client, 
							card, 
							clientCardForm.getCode());
					
					if (clientCard == null) {
						String errorMessage = new StringBuilder()
								.append("Expected a client card returned for client ")
								.append("[" + client.getId() + " - " + client.getName() + "] ")
								.append("but NULL was returned. ")
								.append("Aborting editCards().")
								.toString();
											
						throw new EmptyResultDataAccessException(errorMessage, 1);
					}
					
					if (clientCard.getEnabled() != clientCardForm.getEnabled()) {
						clientCard.setEnabled(clientCardForm.getEnabled());
						clientCard.setModified(LocalDateTime.now());
					
						clientCardRepository.saveAndFlush(clientCard);
					}
				}
			}
		}
		catch (Exception exception) {
			throw new ClientCardException("We are unable to edit your cards.", exception);
		}
	}
}