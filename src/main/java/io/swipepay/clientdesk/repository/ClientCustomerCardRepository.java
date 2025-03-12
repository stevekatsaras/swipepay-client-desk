package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.ClientCustomer;
import io.swipepay.clientdesk.domain.ClientCustomerCard;

public interface ClientCustomerCardRepository extends JpaRepository<ClientCustomerCard, Long> {
	
	Long countByClientCustomer(ClientCustomer clientCustomer);
	
	ClientCustomerCard findByClientCustomerAndCode(ClientCustomer clientCustomer, String code);
	
	List<ClientCustomerCard> findByClientCustomerAndIsDefaultFalse(ClientCustomer clientCustomer);
	
	ClientCustomerCard findByClientCustomerAndIsDefaultTrue(ClientCustomer clientCustomer);
	
	List<ClientCustomerCard> findByClientCustomerOrderByIsDefaultDesc(ClientCustomer clientCustomer);
	
}