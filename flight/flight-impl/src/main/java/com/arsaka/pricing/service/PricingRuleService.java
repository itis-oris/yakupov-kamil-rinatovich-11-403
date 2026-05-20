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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
                log.debug("Pricing rule already exists | fare id={} | passenger type={}", fare.getId(), request.passengerType());
                throw new PricingRuleAlreadyExistsException();
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
        PricingRule pricingRule = findById(id);

        if(!pricingRule.isActive()) {
            log.debug("Pricing rule not active | id={}", id);
            throw new PricingRuleNotFoundException();
        }

        pricingRule.setMultiplier(request.multiplier());

        return mapper.toResponse(pricingRule);
    }

    private PricingRule findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Pricing rule not found exception | id={}", id);
                    return new PricingRuleNotFoundException();
                });
    }


    @Transactional
    public void delete(UUID id) {
        PricingRule pricingRule = findById(id);
        pricingRule.setActive(false);
    }
}
