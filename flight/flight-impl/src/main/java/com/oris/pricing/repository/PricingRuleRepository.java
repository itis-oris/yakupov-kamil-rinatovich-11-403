package com.oris.pricing.repository;

import com.oris.common.PassengerType;
import com.oris.pricing.model.Fare;
import com.oris.pricing.model.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingRuleRepository extends JpaRepository<PricingRule, UUID>, JpaSpecificationExecutor<PricingRule> {
    Optional<PricingRule> findByFareAndPassengerType(Fare fare, PassengerType passengerType);
}
