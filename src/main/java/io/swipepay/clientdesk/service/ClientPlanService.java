package io.swipepay.clientdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.swipepay.clientdesk.config.security.DefaultAuthenticationFacade;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.Currency;
import io.swipepay.clientdesk.domain.Plan;
import io.swipepay.clientdesk.exception.ClientPlanException;
import io.swipepay.clientdesk.form.ClientPlanForm;
import io.swipepay.clientdesk.support.NumberSupport;

@Service
public class ClientPlanService {
	@Autowired
	private DefaultAuthenticationFacade authenticationFacade;
	
	@Autowired
	private NumberSupport numberSupport;
	
	@Transactional(readOnly = true)
	public void init(ClientPlanForm clientPlanForm) throws ClientPlanException {
		try {
			Client client = authenticationFacade.getClientFromDatabase();
			
			Plan plan = client.getPlan();
			Currency currency = plan.getCurrency();
			
			clientPlanForm.setCost(numberSupport.formatMoney(
					plan.getCost(), 
					currency.getIso3(), 
					currency.getDivisible()));
			
			clientPlanForm.setUnit(plan.getUnit());
			clientPlanForm.setTxnCap(Long.toString(plan.getTxnCap()));
			
			clientPlanForm.setTxnCapExcessCost(numberSupport.formatMoney(
					plan.getTxnCapExcessCost(), 
					currency.getIso3(), 
					currency.getDivisible()));
		}
		catch (Exception exception) {
			throw new ClientPlanException("We are unable to get your plan.", exception);
		}
	}
	
}