package com.arsaka.flightsearch.repository;

import com.arsaka.common.CabinClass;
import com.arsaka.flightsearch.model.Flight;
import com.arsaka.flightsearch.model.FlightInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightInventoryCommandRepository extends JpaRepository<FlightInventory, UUID> {
    boolean existsByFlightAndCabinClass(Flight flight, CabinClass cabinClass);
}
