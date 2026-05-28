package com.oris.pricing.mapper;

import com.oris.create.request.CreatePricingAdjustmentRequest;
import com.oris.create.response.PricingAdjustmentResponse;
import com.oris.flight.model.Flight;
import com.oris.pricing.model.Fare;
import com.oris.pricing.model.PricingAdjustment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PricingAdjustmentMapper {

    default PricingAdjustment toEntity(CreatePricingAdjustmentRequest request, Flight flight, Fare fare) {
        return PricingAdjustment.builder()
                .flight(flight)
                .fare(fare)
                .type(request.type())
                .value(request.value())
                .build();
    }

    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "fareId", source = "fare.id")
    PricingAdjustmentResponse toResponse(PricingAdjustment pricingAdjustment);
}