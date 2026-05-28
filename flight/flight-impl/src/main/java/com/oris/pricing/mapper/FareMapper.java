package com.oris.pricing.mapper;

import com.oris.create.request.CreateFareRequest;
import com.oris.create.response.FareResponse;
import com.oris.pricing.model.Fare;
import com.oris.referencedata.model.Airline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FareMapper {

    default Fare toEntity(CreateFareRequest request, Airline airline) {
        return Fare.builder()
                .airline(airline)
                .cabinClass(request.cabinClass())
                .name(request.name())
                .baggageIncluded(request.baggageIncluded())
                .refundable(request.isRefundable())
                .build();
    }

    @Mapping(target = "airlineCode", source = "airline.code")
    FareResponse toResponse(Fare fare);
}