package com.arsaka.pricing.repository;

import com.arsaka.pricing.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FareCommandRepository extends JpaRepository<Fare, UUID> {
}
