package com.arsaka.pricing.repository;

import com.arsaka.flightsearch.model.Flight;
import com.arsaka.pricing.model.Fare;
import com.arsaka.pricing.model.PricingAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingAdjustmentRepository extends JpaRepository<PricingAdjustment, UUID> {
    Optional<PricingAdjustment> findByFlightAndFare(Flight flight, Fare fare);
}
