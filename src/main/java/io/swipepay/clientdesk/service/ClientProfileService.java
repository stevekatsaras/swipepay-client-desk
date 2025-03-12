package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientProfile;
import io.swipepay.clientdesk.domain.enums.ClientProfileStatus;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientProfileException;
import io.swipepay.clientdesk.exception.ClientUserException;
import io.swipepay.clientdesk.form.ClientProfileForm;
import io.swipepay.clientdesk.repository.ClientProfileRepository;

@Service
public class ClientProfileService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientProfileRepository clientProfileRepository;
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientProfileException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<ClientProfile> clientProfiles = clientProfileRepository.findByClient(client);
			
			List<ClientProfileForm> profiles = new ArrayList<ClientProfileForm>();
			for (ClientProfile clientProfile : clientProfiles) {
				ClientProfileForm clientProfileForm = new ClientProfileForm();
				clientProfileForm.setCode(clientProfile.getCode());
				clientProfileForm.setName(clientProfile.getName());
				clientProfileForm.setEnabled(clientProfile.getEnabled());
				clientProfileForm.setStatus(clientProfile.getStatus());
				
				clientProfileForm.setModified(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyyhhmma.toString()).format(
								clientProfile.getModified()));
				
				profiles.add(clientProfileForm);
			}
			modelMap.addAttribute("profiles", profiles);
		}
		catch (Exception exception) {
			throw new ClientProfileException("We are unable to get your profiles.", exception);
		}
	}
	
	public void initProfile(ModelMap modelMap) throws ClientProfileException {
		try {
			modelMap.addAttribute("statuses", ClientProfileStatus.values());
		}
		catch (Exception exception) {
			throw new ClientUserException("We are unable to get the profile metadata.", exception);
		}
	}

	@Transactional(readOnly = false)
	public void addProfile(ClientProfileForm clientProfileForm) throws ClientProfileException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			ClientProfile clientProfile = new ClientProfile(
					"pro_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					clientProfileForm.getName(), 
					clientProfileForm.getEnabled(), 
					clientProfileForm.getStatus(), 
					LocalDateTime.now(), 
					client);
			
			clientProfileRepository.saveAndFlush(clientProfile);
		}
		catch (Exception exception) {
			throw new ClientProfileException("We are unable to add your profile.", exception);
		}
	}

	public void getProfile(String code, ClientProfileForm clientProfileForm) throws ClientProfileException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientProfile clientProfile = clientProfileRepository.findByClientAndCode(client, code);
			
			if (clientProfile == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client profile returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting getProfile().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}

			clientProfileForm.setCode(clientProfile.getCode());
			clientProfileForm.setName(clientProfile.getName());
			clientProfileForm.setEnabled(clientProfile.getEnabled());
			clientProfileForm.setStatus(clientProfile.getStatus());
			
			clientProfileForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							clientProfile.getModified()));
		}
		catch (Exception exception) {
			throw new ClientProfileException("We are unable to get your profile.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void editProfile(String code, ClientProfileForm clientProfileForm) throws ClientProfileException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientProfile clientProfile = clientProfileRepository.findByClientAndCode(client, code);
			
			if (clientProfile == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a client profile returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting editProfile().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientProfile.setName(clientProfileForm.getName());
			clientProfile.setEnabled(clientProfileForm.getEnabled());
			clientProfile.setStatus(clientProfileForm.getStatus());
			clientProfile.setModified(LocalDateTime.now());
			
			clientProfileRepository.saveAndFlush(clientProfile);
		}
		catch (Exception exception) {
			throw new ClientProfileException("We are unable to edit your profile.", exception);
		}
	}
}