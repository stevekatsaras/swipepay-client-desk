package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientUser;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
	
	ClientUser findByClientAndEmailAddress(Client client, String emailAddress);
	
	List<ClientUser> findByClientOrderByEmailAddressAsc(Client client);
	
	ClientUser findByEmailAddress(String emailAddress);
	
	ClientUser findByEmailAddressAndEnabledAndExpiredAndLocked(
			String emailAddress, 
			Boolean enabled, 
			Boolean expired, 
			Boolean locked);
	
}