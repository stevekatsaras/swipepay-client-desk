package io.swipepay.clientdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
	
	Offer findByEnabled(Boolean enabled);
}