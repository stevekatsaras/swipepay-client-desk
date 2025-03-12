package io.swipepay.clientdesk.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.exception.ClientUserException;
import io.swipepay.clientdesk.form.ClientUserForm;
import io.swipepay.clientdesk.repository.ClientUserRepository;

@Component
public class ClientUserValidator implements Validator {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ClientUserForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}
	
	@Transactional(readOnly = true)
	public void validateIfEmailAddressIsRegistered(ClientUserForm clientUserForm, Errors errors) throws ClientUserException {
		try {
			if (!errors.hasFieldErrors("emailAddress")) {
				ClientUser clientUser = clientUserRepository.findByEmailAddress(clientUserForm.getEmailAddress());
			
				if (clientUser != null) {
					errors.rejectValue("emailAddress", "invalid", "Email address is already registered");
				}
			}
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to validate your email address.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateIfDisabledUserIsPrincipal(
			ClientUserForm clientUserForm, 
			Errors errors, 
			String emailAddress) throws ClientUserException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientUser clientUser = clientUserRepository.findByClientAndEmailAddress(client, emailAddress);
			
			if (clientUser == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client user returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting validateIfDisabledUserIsPrincipal().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			ClientUser principalClientUser = authenticationFacade.getClientUserFromPrincipal();
			
			if (StringUtils.equalsIgnoreCase(clientUser.getEmailAddress(), principalClientUser.getEmailAddress()) && 
					clientUserForm.getEnabled() == false) {
				
				errors.rejectValue("enabled", "invalid", "Cannot disable self.");
			}
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to validate your user.", exception);
		}
	}
	
}