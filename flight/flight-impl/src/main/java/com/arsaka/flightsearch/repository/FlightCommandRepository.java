package com.arsaka.flightsearch.repository;

import com.arsaka.flightsearch.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightCommandRepository extends JpaRepository<Flight, UUID> {
}
