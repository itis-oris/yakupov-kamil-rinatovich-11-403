package com.arsaka.pricing.service;

import com.arsaka.create.request.CreatePricingRuleRequest;
import com.arsaka.create.response.PricingRuleResponse;
import com.arsaka.pricing.exception.PricingRuleAlreadyExistsException;
import com.arsaka.pricing.exception.PricingRuleNotFoundException;
import com.arsaka.pricing.mapper.PricingRuleMapper;
import com.arsaka.pricing.model.Fare;
import com.arsaka.pricing.model.PricingRule;
import com.arsaka.pricing.repository.PricingRuleRepository;
import com.arsaka.updaterequest.UpdatePricingRuleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PricingRuleService {

    private final PricingRuleRepository repository;
    private final FareService fareService;
    private final PricingRuleMapper mapper;

    @Transactional
    public PricingRuleResponse create(CreatePricingRuleRequest request) {
        Fare fare = fareService.findById(request.fareId());

        PricingRule pricingRule = repository.findByFareAndPassengerType(fare, request.passengerType()).orElse(null);

        if (pricingRule != null) {
            if (pricingRule.isActive()) {
                throw new PricingRuleAlreadyExistsException(fare.getId(), request.passengerType());
            }
            pricingRule.setActive(true);
            return mapper.toResponse(pricingRule);
        }

        pricingRule = mapper.toEntity(request, fare);

        PricingRule saved = repository.save(pricingRule);

        return mapper.toResponse(saved);
    }

    @Transactional
    public PricingRuleResponse update(UUID id, UpdatePricingRuleRequest request) {
        PricingRule pricingRule = repository.findById(id)
                .orElseThrow(() -> new PricingRuleNotFoundException(id));

        if(!pricingRule.isActive()) {
            throw new PricingRuleNotFoundException(id);
        }

        pricingRule.setMultiplier(request.multiplier());

        return mapper.toResponse(pricingRule);
    }


    @Transactional
    public void delete(UUID id) {
        PricingRule pricingRule = repository.findById(id)
                .orElseThrow(() -> new PricingRuleNotFoundException(id));

        pricingRule.setActive(false);
    }
}
