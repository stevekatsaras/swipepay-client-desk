package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCustomer;

public interface ClientCustomerRepository extends JpaRepository<ClientCustomer, Long> {
	
	List<ClientCustomer> findByClient(Client client);
	
	ClientCustomer findByClientAndCode(Client client, String code);
}