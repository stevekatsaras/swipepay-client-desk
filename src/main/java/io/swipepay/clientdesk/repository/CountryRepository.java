package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
	
	List<Country> findAllByOrderByNameAsc();
}