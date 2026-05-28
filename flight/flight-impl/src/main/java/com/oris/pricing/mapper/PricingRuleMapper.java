package com.oris.pricing.mapper;

import com.oris.create.request.CreatePricingRuleRequest;
import com.oris.create.response.PricingRuleResponse;
import com.oris.pricing.model.Fare;
import com.oris.pricing.model.PricingRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PricingRuleMapper {

    default PricingRule toEntity(CreatePricingRuleRequest request, Fare fare) {
        return PricingRule.builder()
                .fare(fare)
                .passengerType(request.passengerType())
                .multiplier(request.multiplier())
                .build();
    }

    @Mapping(target = "fareId", source = "fare.id")
    PricingRuleResponse toResponse(PricingRule pricingRule);
}