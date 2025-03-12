package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientCurrency;
import io.swipepay.clientdesk.domain.Currency;

public interface ClientCurrencyRepository extends JpaRepository<ClientCurrency, Long> {
	
	List<ClientCurrency> findByClient(Client client);
	
	ClientCurrency findByClientAndCurrency(Client client, Currency currency);
	
	ClientCurrency findByClientAndCurrencyAndCode(Client client, Currency currency, String code);
	
	List<ClientCurrency> findByClientAndEnabled(Client client, Boolean enabled);
}