package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCard;

public interface ClientCardRepository extends JpaRepository<ClientCard, Long> {
	
	ClientCard findByClientAndCard(Client client, Card card);

	ClientCard findByClientAndCardAndCode(Client client, Card card, String code);
	
	List<ClientCard> findByClientAndEnabled(Client client, Boolean enabled);
	
}