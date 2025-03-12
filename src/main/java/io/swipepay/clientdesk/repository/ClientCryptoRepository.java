package io.swipepay.clientdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCrypto;

public interface ClientCryptoRepository extends JpaRepository<ClientCrypto, Long> {
	
	ClientCrypto findByClientAndEnabled(Client client, Boolean enabled);
	
}