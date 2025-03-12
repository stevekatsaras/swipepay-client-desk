package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
	
	Plan findByTypeAndCostTypeAndId(String type, String costType, Long id);
	
	List<Plan> findByTypeAndCostTypeOrderByCostAsc(String type, String costType);
}