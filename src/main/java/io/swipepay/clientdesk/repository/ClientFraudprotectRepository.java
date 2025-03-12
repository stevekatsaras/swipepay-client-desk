package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientFraudprotect;

public interface ClientFraudprotectRepository extends JpaRepository<ClientFraudprotect, Long> {
	
	List<ClientFraudprotect> findByClient(Client client);

	ClientFraudprotect findByClientAndCode(Client client, String code);
}