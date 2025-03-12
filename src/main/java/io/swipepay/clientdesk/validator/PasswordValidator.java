package io.swipepay.clientdesk.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.domain.enums.ClientStatus;
import io.swipepay.clientdesk.exception.PasswordException;
import io.swipepay.clientdesk.form.PasswordForm;
import io.swipepay.clientdesk.repository.ClientUserRepository;
import io.swipepay.clientdesk.support.PasswordSupport;

@Component
public class PasswordValidator implements Validator {
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Autowired
	private PasswordSupport passwordSupport;

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}
	
	public void validateResetPassword(Object target, Errors errors) throws PasswordException {
		PasswordForm passwordForm = (PasswordForm) target;
		try {
			validateClientUser(passwordForm, errors);
		}
		catch (Exception exception) {
			throw new PasswordException("We are unable to validate your email address.", exception);
		}
	}
	
	public void validateChangePassword(Object target, Errors errors) throws PasswordException {
		PasswordForm passwordForm = (PasswordForm) target;
		try {
			ClientUser clientUser = validateClientUser(passwordForm, errors);
			validatePasswords(passwordForm, errors, clientUser);
		}
		catch (Exception exception) {
			throw new PasswordException("We are unable to validate your email address and/or password.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	private ClientUser validateClientUser(PasswordForm passwordForm, Errors errors) {
		ClientUser clientUser = null;
		if (!errors.hasFieldErrors("emailAddress")) {
			clientUser = clientUserRepository.findByEmailAddressAndEnabledAndExpiredAndLocked(
					passwordForm.getEmailAddress(), 
					true, // enabled 
					false, // not expired
					false); // not locked
			
			if (clientUser == null) {
				errors.rejectValue("emailAddress", "invalid", "Email address is invalid");
			}
			else if (!StringUtils.equals(clientUser.getClient().getStatus(), ClientStatus.Active.name())) {
				errors.rejectValue("emailAddress", "invalid", "Client account is not active.");
			}
		}
		return clientUser;
	}
	
	private void validatePasswords(PasswordForm passwordForm, Errors errors, ClientUser clientUser) {
		if (!errors.hasFieldErrors("currentPassword") && clientUser != null) {
			
			Boolean passwordsMatch = passwordSupport.matches(
					passwordForm.getCurrentPassword(), 
					clientUser.getPassword());
			
			if (!passwordsMatch) {
				errors.rejectValue("currentPassword", "invalid", "Current password is invalid");
			}
		}
		
		if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("confirmNewPassword")) {
			
			if (!passwordSupport.isStrong(passwordForm.getNewPassword())) {
				errors.rejectValue("newPassword", "invalid", "New password is not strong enough");
			}
			else if (!StringUtils.equals(passwordForm.getNewPassword(), passwordForm.getConfirmNewPassword())) {
				errors.rejectValue("confirmNewPassword", "invalid", "Confirm new password does not match");
			}
		}
	}
}