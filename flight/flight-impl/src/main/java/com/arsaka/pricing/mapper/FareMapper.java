package com.arsaka.pricing.mapper;

import com.arsaka.create.request.CreateFareRequest;
import com.arsaka.create.response.FareResponse;
import com.arsaka.pricing.model.Fare;
import com.arsaka.referencedata.model.Airline;
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