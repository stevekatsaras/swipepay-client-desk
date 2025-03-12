package io.swipepay.clientdesk.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.repository.ClientRepository;
import io.swipepay.clientdesk.repository.ClientUserRepository;

@Component
public class DefaultAuthenticationFacade {
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public DefaultUserDetails getPrincipal() {
		return (DefaultUserDetails) getAuthentication().getPrincipal();
	}
	
	/**
	 * Returns a cached copy of the client that is contained in the principal.
	 * We recommend using this only if you require the client ID to make 
	 * database lookups using this as the foreign key.
	 * All other information in this object may be outdated and should not be 
	 * trusted. Instead, we recommend using the getClientFromDatabase() if 
	 * you wish to return an up-to-date version of a clients' contents.
	 * @return the client cached in the principal
	 */
	public Client getClientFromPrincipal() {
		return getPrincipal().getClient();
	}
	
	/**
	 * Returns a database copy of the client.
	 * We recommend using this only if you require up-to-date client information
	 * that is intended to be used for some business function. If all you require
	 * if the client ID, please use the getClientFromPrincipal() instead.
	 * Not required to make a database roundtrip for an ID that will never change.
	 * @return the client from the database
	 */
	@Transactional(readOnly = true)
	public Client getClientFromDatabase() {
		return clientRepository.getOne(getClientFromPrincipal().getId());
	}
	
	/**
	 * Returns a cached copy of the client user that is contained in the principal.
	 * We recommend using this only if you require the client user ID to make 
	 * database lookup using this as a foriegn key.
	 * All other information in this object may be outdated and should not be 
	 * trusted. Instead, we recommend using the getClientUserFromDatabase() if 
	 * you wish to return an up-to-date version of a clients users' contents.
	 * @return the client user cached in the principal
	 */
	public ClientUser getClientUserFromPrincipal() {
		return getPrincipal().getClientUser();
	}
	
	/**
	 * Returns a database copy of the client user.
	 * We recommend using this only if you require up-to-date client user information
	 * that is intended to be used for some business function. If all you require
	 * if the client user ID, please use the getClientUserFromPrincipal() instead.
	 * Not required to make a database roundtrip for an ID that will never change.
	 * @return the client user from the database
	 */
	@Transactional(readOnly = true)
	public ClientUser getClientUserFromDatabase() {
		return clientUserRepository.getOne(getClientUserFromPrincipal().getId());
	}
}