package com.oris.pricing.repository;

import com.oris.pricing.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FareCommandRepository extends JpaRepository<Fare, UUID>, JpaSpecificationExecutor<Fare> {
}
