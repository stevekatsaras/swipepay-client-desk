package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.domain.enums.ClientUserRole;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientUserException;
import io.swipepay.clientdesk.form.ClientUserForm;
import io.swipepay.clientdesk.repository.ClientUserRepository;
import io.swipepay.clientdesk.support.PasswordSupport;

@Service
public class ClientUserService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Autowired
	private PasswordSupport passwordSupport;
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientUserException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<ClientUser> clientUsers = clientUserRepository.findByClientOrderByEmailAddressAsc(client);
			
			List<ClientUserForm> users = new ArrayList<ClientUserForm>();
			for (ClientUser clientUser : clientUsers) {
				ClientUserForm clientUserForm = new ClientUserForm();
				clientUserForm.setEmailAddress(clientUser.getEmailAddress());
				clientUserForm.setFirstname(clientUser.getFirstname());
				clientUserForm.setLastname(clientUser.getLastname());
				clientUserForm.setTelephone(clientUser.getTelephone());
				clientUserForm.setMobile(clientUser.getMobile());
				clientUserForm.setEnabled(clientUser.getEnabled());
				clientUserForm.setRole(clientUser.getRole());
				
				clientUserForm.setModified(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyyhhmma.toString()).format(
								clientUser.getModified()));
				
				users.add(clientUserForm);
			}
			modelMap.addAttribute("users", users);
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to get your users.", exception);
		}
	}
	
	public void initUser(ModelMap modelMap) throws ClientUserException {
		try {
			modelMap.addAttribute("roles", ClientUserRole.values());
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to get the user metadata.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void addUser(ClientUserForm clientUserForm) throws ClientUserException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			String clearTextPassword = passwordSupport.generate();
			
			ClientUser clientUser = new ClientUser(
					clientUserForm.getEmailAddress(), 
					clientUserForm.getFirstname(), 
					clientUserForm.getLastname(), 
					clientUserForm.getTelephone(), 
					clientUserForm.getMobile(), 
					passwordSupport.hash(clearTextPassword), 
					clientUserForm.getEnabled(), 
					false, // not expired 
					false, // not locked 
					true, // password expired (temporary password)
					clientUserForm.getRole(), 
					LocalDateTime.now(),
					client);
			
			clientUserRepository.saveAndFlush(clientUser);
			
			//TODO send email with the temporary password
			
			System.out.println("email address:" + clientUserForm.getEmailAddress()); 
			System.out.println("temporary password:" + clearTextPassword);
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to add your user.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void getUser(String emailAddress, ClientUserForm clientUserForm) throws ClientUserException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientUser clientUser = clientUserRepository.findByClientAndEmailAddress(client, emailAddress);
			
			if (clientUser == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client user returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting getUser().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientUserForm.setEmailAddress(clientUser.getEmailAddress());
			clientUserForm.setFirstname(clientUser.getFirstname());
			clientUserForm.setLastname(clientUser.getLastname());
			clientUserForm.setTelephone(clientUser.getTelephone());
			clientUserForm.setMobile(clientUser.getMobile());
			clientUserForm.setEnabled(clientUser.getEnabled());
			clientUserForm.setRole(clientUser.getRole());
			
			clientUserForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientUser.getModified()));
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to get your user.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void editUser(String emailAddress, ClientUserForm clientUserForm) throws ClientUserException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientUser clientUser = clientUserRepository.findByClientAndEmailAddress(client, emailAddress);
			
			if (clientUser == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client user returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting editUser().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientUser.setEmailAddress(clientUserForm.getEmailAddress());
			clientUser.setFirstname(clientUserForm.getFirstname());
			clientUser.setLastname(clientUserForm.getLastname());
			clientUser.setTelephone(clientUserForm.getTelephone());
			clientUser.setMobile(clientUserForm.getMobile());
			clientUser.setEnabled(clientUserForm.getEnabled());
			clientUser.setRole(clientUserForm.getRole());
			clientUser.setModified(LocalDateTime.now());
			
			if (clientUserForm.getResetPassword()) {
				String clearTextPassword = passwordSupport.generate();
				
				clientUser.setPassword(passwordSupport.hash(clearTextPassword));
				clientUser.setPasswordExpired(true); // password expired (temporary password)
				
				System.out.println("email address:" + clientUserForm.getEmailAddress());
				System.out.println("temporary password:" + clearTextPassword);
				
				// TODO: send email to user with temporary password!
			}
			clientUserRepository.saveAndFlush(clientUser);
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to edit your user.", exception);
		}
	}
}