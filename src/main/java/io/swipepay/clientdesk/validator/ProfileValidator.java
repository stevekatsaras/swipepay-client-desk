package io.swipepay.clientdesk.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.exception.ProfileException;
import io.swipepay.clientdesk.form.ProfileForm;
import io.swipepay.clientdesk.support.PasswordSupport;

@Component
public class ProfileValidator implements Validator {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private PasswordSupport passwordSupport;

	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}
	
	public void validatePasswords(Object target, Errors errors) throws ProfileException {
		ProfileForm profileForm = (ProfileForm) target;
		try {
			if (!errors.hasFieldErrors("currentPassword")) {
				ClientUser clientUser = authenticationFacade.getClientUserFromDatabase();
				
				Boolean passwordsMatch = passwordSupport.matches(
						profileForm.getCurrentPassword(), 
						clientUser.getPassword());
			
				if (!passwordsMatch) {
					errors.rejectValue("currentPassword", "invalid", "Current password is invalid");
				}
			}
			
			if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("confirmNewPassword")) {
				
				if (!passwordSupport.isStrong(profileForm.getNewPassword())) {
					errors.rejectValue("newPassword", "invalid", "New password is not strong enough");
				}
				else {
					if (!StringUtils.equals(profileForm.getNewPassword(), profileForm.getConfirmNewPassword())) {
						errors.rejectValue("confirmNewPassword", "invalid", "Confirm new password does not match");
					}
				}	
			}
		}
		catch (Exception exception) {
			throw new ProfileException("We are unable to validate your email address and/or password.", exception);
		}
	}
}