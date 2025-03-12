package io.swipepay.clientdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.TransactionResponse;

public interface TransactionResponseRepository extends JpaRepository<TransactionResponse, Long> {

}