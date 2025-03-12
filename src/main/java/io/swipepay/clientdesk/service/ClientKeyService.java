package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.config.security.DefaultUserDetails;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientKey;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientKeyException;
import io.swipepay.clientdesk.form.ClientKeyForm;
import io.swipepay.clientdesk.form.ClientPrivateKeyForm;
import io.swipepay.clientdesk.repository.ClientKeyRepository;
import io.swipepay.clientdesk.repository.ClientRepository;

@Service
public class ClientKeyService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientKeyRepository clientKeyRepository;
	
	@Transactional(readOnly = true)
	public void initPublicKeys(ModelMap modelMap) throws ClientKeyException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<ClientKey> publicClientKeys = clientKeyRepository.findByClient(client);
			
			List<ClientKeyForm> publicKeys = new ArrayList<ClientKeyForm>();
			for (ClientKey publicClientKey : publicClientKeys) {
				ClientKeyForm clientKeyForm = new ClientKeyForm();
				clientKeyForm.setCode(publicClientKey.getCode());
				clientKeyForm.setEnabled(publicClientKey.getEnabled());
				
				clientKeyForm.setModified(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyyhhmma.toString()).format(
								publicClientKey.getModified()));
					
				publicKeys.add(clientKeyForm);
			}
			modelMap.addAttribute("pubkeys", publicKeys);
		}
		catch (Exception exception) {
			throw new ClientKeyException("We are unable to get your public keys.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void initPrivateKey(ClientPrivateKeyForm clientPrivateKeyForm) throws ClientKeyException {
		try {
			Client client = authenticationFacade.getClientFromDatabase();
			
			clientPrivateKeyForm.setPrivateKey(client.getPrivateKey());
		}
		catch (Exception exception) {
			throw new ClientKeyException("We are unable to get your private key.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void addPublicKey() throws ClientKeyException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			
			ClientKey publicClientKey = new ClientKey(
					"pub_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					true, // enabled
					LocalDateTime.now(), 
					client);
			
			clientKeyRepository.saveAndFlush(publicClientKey);
		}
		catch (Exception exception) {
			throw new ClientKeyException("We are unable to add a public key.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void getPublicKey(String code, ClientKeyForm clientKeyForm) throws ClientKeyException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientKey publicClientKey = clientKeyRepository.findByClientAndCode(client, code);
			
			if (publicClientKey == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a public client key returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting getPublicKey().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			clientKeyForm.setCode(publicClientKey.getCode());
			clientKeyForm.setEnabled(publicClientKey.getEnabled());
			
			clientKeyForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							publicClientKey.getModified()));
		}
		catch (Exception exception) {
			throw new ClientKeyException("We are unable to get your public key.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void editPublicKey(String code, ClientKeyForm clientKeyForm) throws ClientKeyException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			ClientKey publicClientKey = clientKeyRepository.findByClientAndCode(client, code);
			
			if (publicClientKey == null) {
				String errorMessage = new StringBuilder()
						.append("Expected a public client key returned for client ")
						.append("[" + client.getId() + " - " + client.getName() + "] ")
						.append("but NULL was returned. ")
						.append("Aborting editPublicKey().")
						.toString();
									
				throw new EmptyResultDataAccessException(errorMessage, 1);
			}
			
			publicClientKey.setEnabled(clientKeyForm.getEnabled());
			publicClientKey.setModified(LocalDateTime.now());
			
			clientKeyRepository.saveAndFlush(publicClientKey);
		}
		catch (Exception exception) {
			throw new ClientKeyException("We are unable to edit your public key.", exception);
		}
	}

	@Transactional(readOnly = false)
	public void resetPrivateKey() throws ClientKeyException {
		try {
			Client client = authenticationFacade.getClientFromDatabase();
			
			client.setPrivateKey("priv_" + UUID.randomUUID().toString().replaceAll("-", ""));
			client.setModified(LocalDateTime.now());
			
			clientRepository.saveAndFlush(client);
			
			DefaultUserDetails defaultUserDetails = authenticationFacade.getPrincipal();
			defaultUserDetails.setClient(client);
			
			authenticationFacade.setAuthentication(new UsernamePasswordAuthenticationToken(
					defaultUserDetails, 
					defaultUserDetails.getPassword(), 
					defaultUserDetails.getAuthorities()));
		}
		catch (Exception exception) {
			throw new ClientKeyException("We are unable to reset your private key.", exception);
		}
	}
}