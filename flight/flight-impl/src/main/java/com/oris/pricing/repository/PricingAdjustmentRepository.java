package com.oris.pricing.repository;

import com.oris.flight.model.Flight;
import com.oris.pricing.model.Fare;
import com.oris.pricing.model.PricingAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingAdjustmentRepository extends JpaRepository<PricingAdjustment, UUID>, JpaSpecificationExecutor<PricingAdjustment> {
    Optional<PricingAdjustment> findByFlightAndFare(Flight flight, Fare fare);
}
