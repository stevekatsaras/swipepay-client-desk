package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.TransactionType;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
	
	List<TransactionType> findByNewTxn(Boolean newTxn);
}