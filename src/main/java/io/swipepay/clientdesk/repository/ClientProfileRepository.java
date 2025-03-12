package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientProfile;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
	
	List<ClientProfile> findByClient(Client client);
	
	List<ClientProfile> findByClientAndEnabled(Client client, Boolean enabled);
	
	ClientProfile findByClientAndCode(Client client, String code);
	
}