package io.swipepay.clientdesk.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientBilling;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.exception.ClientBillingException;
import io.swipepay.clientdesk.form.ClientBillingForm;
import io.swipepay.clientdesk.repository.ClientBillingRepository;

@Service
public class ClientBillingService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ClientBillingRepository clientBillingRepository;
	
	@Transactional(readOnly = true)
	public void init(ModelMap modelMap) throws ClientBillingException {
		try {
			Client client = authenticationFacade.getClientFromPrincipal();
			List<ClientBilling> clientBilling = clientBillingRepository.findByClientOrderByPeriodFromDesc(client);
			
			List<ClientBillingForm> bills = new ArrayList<>();
			for (ClientBilling cb : clientBilling) {
				ClientBillingForm bill = new ClientBillingForm();
				bill.setCode(cb.getCode());
				
				bill.setPeriodFromFormatted(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyy.toString()).format(
								cb.getPeriodFrom()));
				
				bill.setPeriodToFormatted(DateTimeFormatter.ofPattern(
						DateTimePattern.dMMMyyyy.toString()).format(
								cb.getPeriodTo()));
				
				bill.setStatus(cb.getStatus());
				bills.add(bill);
			}
			modelMap.addAttribute("bills", bills);
		}
		catch (Exception exception) {
			throw new ClientBillingException("We are unable to get your bills.", exception);
		}
	}
}