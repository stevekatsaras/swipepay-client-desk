package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
	
	List<Card> findByTypeOrderByBrandAsc(String type);
}