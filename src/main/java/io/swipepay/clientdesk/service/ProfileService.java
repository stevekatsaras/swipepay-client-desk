package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.config.security.DefaultUserDetails;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ProfileException;
import io.swipepay.clientdesk.form.ProfileForm;
import io.swipepay.clientdesk.repository.ClientUserRepository;
import io.swipepay.clientdesk.support.PasswordSupport;

@Service
public class ProfileService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Autowired
	private PasswordSupport passwordSupport;
	
	@Transactional(readOnly = true)
	public void init(ProfileForm profileForm) throws ProfileException {
		try {
			ClientUser clientUser = authenticationFacade.getClientUserFromDatabase();
			
			profileForm.setEmailAddress(clientUser.getEmailAddress());
			profileForm.setFirstname(clientUser.getFirstname());
			profileForm.setLastname(clientUser.getLastname());
			profileForm.setTelephone(clientUser.getTelephone());
			profileForm.setMobile(clientUser.getMobile());
			profileForm.setRole(clientUser.getRole());
			
			profileForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientUser.getModified()));
		}
		catch (Exception exception) {
			throw new ProfileException("We are unable to get your profile.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void editProfile(ProfileForm profileForm) throws ProfileException {
		try {
			ClientUser clientUser = authenticationFacade.getClientUserFromDatabase();
			clientUser.setEmailAddress(profileForm.getEmailAddress());
			clientUser.setFirstname(profileForm.getFirstname());
			clientUser.setLastname(profileForm.getLastname());
			clientUser.setTelephone(profileForm.getTelephone());
			clientUser.setMobile(profileForm.getMobile());
			clientUser.setModified(LocalDateTime.now());
			
			clientUserRepository.saveAndFlush(clientUser);
			
			DefaultUserDetails defaultUserDetails = authenticationFacade.getPrincipal();
			defaultUserDetails.setClientUser(clientUser);
		
			authenticationFacade.setAuthentication(new UsernamePasswordAuthenticationToken(
					defaultUserDetails, 
					defaultUserDetails.getPassword(), 
					defaultUserDetails.getAuthorities()));
		}
		catch (Exception exception) {
			throw new ProfileException("We are unable to edit your profile.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void changePassword(ProfileForm profileForm) throws ProfileException {
		try {
			ClientUser clientUser = authenticationFacade.getClientUserFromDatabase();
			clientUser.setPassword(passwordSupport.hash(profileForm.getNewPassword()));
			clientUser.setPasswordExpired(false);
			clientUser.setModified(LocalDateTime.now());
			
			clientUserRepository.saveAndFlush(clientUser);
			
			DefaultUserDetails defaultUserDetails = authenticationFacade.getPrincipal();
			defaultUserDetails.setClientUser(clientUser);
			
			authenticationFacade.setAuthentication(new UsernamePasswordAuthenticationToken(
					defaultUserDetails, 
					clientUser.getPassword(), 
					defaultUserDetails.getAuthorities()));
		}
		catch (Exception exception) {
			throw new ProfileException("We are unable to change your password.", exception);
		}
	}
}