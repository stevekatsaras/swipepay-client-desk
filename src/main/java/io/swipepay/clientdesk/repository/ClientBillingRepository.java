package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientBilling;

public interface ClientBillingRepository extends JpaRepository<ClientBilling, Long> {
	
	List<ClientBilling> findByClientOrderByPeriodFromDesc(Client client);
	
}