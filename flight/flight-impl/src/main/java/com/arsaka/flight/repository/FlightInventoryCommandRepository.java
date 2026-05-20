package com.arsaka.flight.repository;

import com.arsaka.common.CabinClass;
import com.arsaka.flight.model.Flight;
import com.arsaka.flight.model.FlightInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightInventoryCommandRepository extends JpaRepository<FlightInventory, UUID>, JpaSpecificationExecutor<FlightInventory> {
    boolean existsByFlightAndCabinClass(Flight flight, CabinClass cabinClass);
}
