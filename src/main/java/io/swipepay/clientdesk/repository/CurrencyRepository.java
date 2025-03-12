package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	
	List<Currency> findAllByOrderByIso3Asc();
}