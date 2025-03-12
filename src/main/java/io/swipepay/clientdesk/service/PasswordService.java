package io.swipepay.clientdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.exception.PasswordException;
import io.swipepay.clientdesk.form.PasswordForm;
import io.swipepay.clientdesk.repository.ClientUserRepository;
import io.swipepay.clientdesk.support.PasswordSupport;

@Service
public class PasswordService {
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Autowired
	private PasswordSupport passwordSupport;
	
	@Transactional(readOnly = false)
	public void reset(PasswordForm passwordForm) throws PasswordException {
		try {
			ClientUser clientUser = clientUserRepository.findByEmailAddressAndEnabledAndExpiredAndLocked(
					passwordForm.getEmailAddress(), 
					true, // enabled 
					false, // not expired
					false); // not locked
			
			String clearTextPassword = passwordSupport.generate();
			
			clientUser.setPassword(passwordSupport.hash(clearTextPassword));
			clientUser.setPasswordExpired(true); // password expired (temporary password)
			
			clientUserRepository.saveAndFlush(clientUser);
			
			System.out.println("reset password:" + clearTextPassword);
			
			//TODO: send email!
			//TODO: perhaps the signup email can be an 'ApplicationEvent' with a listener attached!
		}
		catch (Exception exception) {
			throw new PasswordException("We are unable to reset your password.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void change(PasswordForm passwordForm) throws PasswordException {
		try {
			ClientUser clientUser = clientUserRepository.findByEmailAddressAndEnabledAndExpiredAndLocked(
					passwordForm.getEmailAddress(), 
					true, // enabled 
					false, // not expired
					false); // not locked
			
			clientUser.setPassword(passwordSupport.hash(passwordForm.getNewPassword()));
			clientUser.setPasswordExpired(false);
			
			clientUserRepository.saveAndFlush(clientUser);
		}
		catch (Exception exception) {
			throw new PasswordException("We are unable to change your password.", exception);
		}
	}
}