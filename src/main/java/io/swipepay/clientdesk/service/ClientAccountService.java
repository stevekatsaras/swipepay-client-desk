package io.swipepay.clientdesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.config.security.DefaultUserDetails;
import io.swipepay.clientdesk.domain.BusinessEntity;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.Country;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientAccountException;
import io.swipepay.clientdesk.form.ClientAccountForm;
import io.swipepay.clientdesk.repository.BusinessEntityRepository;
import io.swipepay.clientdesk.repository.ClientRepository;
import io.swipepay.clientdesk.repository.CountryRepository;

@Service
public class ClientAccountService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private BusinessEntityRepository businessEntityRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Transactional(readOnly = true)
	public void init(ClientAccountForm clientAccountForm) throws ClientAccountException {
		try {
			Client client = authenticationFacade.getClientFromDatabase();
			
			clientAccountForm.setClientName(client.getName());
			clientAccountForm.setBusinessEntityId(Long.toString(client.getBusinessEntity().getId()));
			clientAccountForm.setBusinessEntityName(client.getBusinessEntity().getName());
			clientAccountForm.setBusinessEntityNumber(client.getBusinessEntityNumber());
			clientAccountForm.setWebsite(client.getWebsite());
			clientAccountForm.setAddress1(client.getAddress1());
			clientAccountForm.setAddress2(client.getAddress2());
			clientAccountForm.setCity(client.getCity());
			clientAccountForm.setState(client.getState());
			clientAccountForm.setPostcode(client.getPostcode());
			clientAccountForm.setCountryId(Long.toString(client.getCountry().getId()));
			clientAccountForm.setCountryName(client.getCountry().getName());
			clientAccountForm.setStatus(client.getStatus());
			
			clientAccountForm.setSigned(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyy.toString()).format(
							client.getSignupDate()));
			
			clientAccountForm.setModified(DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyyhhmma.toString()).format(
							client.getModified()));
		}
		catch (Exception exception) {
			throw new ClientAccountException("We are unable to get your account.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientAccountException {
		try {
			modelMap.addAttribute("businessEntities", businessEntityRepository.findAllByOrderByNameAsc());
			modelMap.addAttribute("countries", countryRepository.findAllByOrderByNameAsc());
		}
		catch (Exception exception) {
			throw new ClientAccountException("We are unable to get your account metadata.", exception);
		}
	}
	
	@Transactional(readOnly = false)
	public void editAccount(ClientAccountForm clientAccountForm) throws ClientAccountException {
		try {
			BusinessEntity businessEntity = businessEntityRepository.getOne(Long.parseLong(clientAccountForm.getBusinessEntityId()));
			Country country = countryRepository.getOne(Long.parseLong(clientAccountForm.getCountryId()));
			
			Client client = authenticationFacade.getClientFromDatabase();
			client.setName(clientAccountForm.getClientName());
			client.setBusinessEntity(businessEntity);
			client.setBusinessEntityNumber(clientAccountForm.getBusinessEntityNumber());
			client.setWebsite(clientAccountForm.getWebsite());
			client.setAddress1(clientAccountForm.getAddress1());
			client.setAddress2(clientAccountForm.getAddress2());
			client.setCity(clientAccountForm.getCity());
			client.setState(clientAccountForm.getState());
			client.setPostcode(clientAccountForm.getPostcode());
			client.setCountry(country);
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
			throw new ClientAccountException("We are unable to edit your account.", exception);
		}
	}
}