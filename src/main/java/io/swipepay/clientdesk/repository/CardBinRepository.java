package io.swipepay.clientdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.CardBin;

public interface CardBinRepository extends JpaRepository<CardBin, Long> {
	
	CardBin findByBin(String bin);
}