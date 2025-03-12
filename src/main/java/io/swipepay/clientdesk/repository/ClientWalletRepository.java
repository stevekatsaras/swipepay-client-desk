package io.swipepay.clientdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientWallet;

public interface ClientWalletRepository extends JpaRepository<ClientWallet, Long> {
	
	ClientWallet findByClient(Client client);
}