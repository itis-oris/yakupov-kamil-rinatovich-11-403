package com.arsaka.pricing.mapper;

import com.arsaka.create.request.CreatePricingAdjustmentRequest;
import com.arsaka.create.response.PricingAdjustmentResponse;
import com.arsaka.flight.model.Flight;
import com.arsaka.pricing.model.Fare;
import com.arsaka.pricing.model.PricingAdjustment;
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