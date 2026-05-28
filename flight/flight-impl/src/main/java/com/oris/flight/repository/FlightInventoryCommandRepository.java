package com.oris.flight.repository;

import com.oris.common.CabinClass;
import com.oris.flight.model.Flight;
import com.oris.flight.model.FlightInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightInventoryCommandRepository extends JpaRepository<FlightInventory, UUID>, JpaSpecificationExecutor<FlightInventory> {
    boolean existsByFlightAndCabinClass(Flight flight, CabinClass cabinClass);
}
