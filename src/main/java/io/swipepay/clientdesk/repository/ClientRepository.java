package io.swipepay.clientdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}