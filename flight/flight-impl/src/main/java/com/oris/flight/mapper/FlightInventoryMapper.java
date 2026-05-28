package com.oris.flight.mapper;

import com.oris.create.request.CreateFlightInventoryRequest;
import com.oris.create.response.FlightInventoryResponse;
import com.oris.flight.model.Flight;
import com.oris.flight.model.FlightInventory;
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
