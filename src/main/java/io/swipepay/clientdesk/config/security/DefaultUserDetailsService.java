package io.swipepay.clientdesk.config.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.domain.enums.ClientStatus;
import io.swipepay.clientdesk.repository.ClientUserRepository;

//import io.swipepay.clientdesk.web.config.ClientDeskConfig;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	//@Autowired 
	//ClientDeskConfig clientDeskConfig;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
		System.out.println("emailAddress:" + emailAddress);
		
		ClientUser clientUser = null;
		try {
			clientUser = clientUserRepository.findByEmailAddressAndEnabledAndExpiredAndLocked(
					emailAddress, 
					true, // enabled 
					false, // not expired 
					false); // not locked
			
			if (clientUser == null) {
				throw new UsernameNotFoundException("Email address is invalid.");
			}
			else if (!StringUtils.equals(clientUser.getClient().getStatus(), ClientStatus.Active.name())) {
				throw new UsernameNotFoundException("Client account is not active.");
			}
		}
		catch (Exception exception) {
			throw new UsernameNotFoundException("Unable to load client user.", exception);
		}
		return new DefaultUserDetails(clientUser);
		
		// TODO: need to check if username has suffix "-management" and password!
		// this will help us decide whether our internal support team are 'simulating' a real-user login
		// or not - this is to assist in seeing what the merchant sees (if we need to help them with issues)
		
		/*return new ClientUserDetails(
				"key", 
				username, 
				"$2a$08$Y3mJXRYkzp9WH4veVode6OKDmtc0sie3PVl7GbUun2gf9xJjd3Qci", 
				grantedAuthorities, true, true, true, true);*/
	}
}