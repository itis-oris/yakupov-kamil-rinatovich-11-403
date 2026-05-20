package com.arsaka.flight.mapper;

import com.arsaka.create.request.CreateFlightInventoryRequest;
import com.arsaka.create.response.FlightInventoryResponse;
import com.arsaka.flight.model.Flight;
import com.arsaka.flight.model.FlightInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightInventoryMapper {

    default FlightInventory toEntity(CreateFlightInventoryRequest request, Flight flight) {
        return FlightInventory.builder()
                .flight(flight)
                .cabinClass(request.cabinClass())
                .price(request.price())
                .build();
    }

    @Mapping(target = "flightId", source = "flight.id")
    FlightInventoryResponse toResponse(FlightInventory flightInventory);
}
