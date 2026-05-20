package com.arsaka.flight.repository;

import com.arsaka.flight.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightCommandRepository extends JpaRepository<Flight, UUID>, JpaSpecificationExecutor<Flight> {
}
