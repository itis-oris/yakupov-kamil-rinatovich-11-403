package com.arsaka.pricing.mapper;

import com.arsaka.create.request.CreatePricingRuleRequest;
import com.arsaka.create.response.PricingRuleResponse;
import com.arsaka.pricing.model.Fare;
import com.arsaka.pricing.model.PricingRule;
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