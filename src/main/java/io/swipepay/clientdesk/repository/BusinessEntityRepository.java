package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.BusinessEntity;

public interface BusinessEntityRepository extends JpaRepository<BusinessEntity, Long> {
	
	List<BusinessEntity> findAllByOrderByNameAsc();
}