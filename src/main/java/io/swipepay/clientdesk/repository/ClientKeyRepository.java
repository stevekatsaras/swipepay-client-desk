package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientKey;

public interface ClientKeyRepository extends JpaRepository<ClientKey, Long> {
		
	List<ClientKey> findByClient(Client client);
		
	ClientKey findByClientAndCode(Client client, String code);
	
}