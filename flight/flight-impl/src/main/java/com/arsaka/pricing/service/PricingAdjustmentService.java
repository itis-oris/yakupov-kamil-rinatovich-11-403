package com.arsaka.pricing.service;

import com.arsaka.create.request.CreatePricingAdjustmentRequest;
import com.arsaka.create.response.PricingAdjustmentResponse;
import com.arsaka.flightsearch.model.Flight;
import com.arsaka.flightsearch.service.FlightService;
import com.arsaka.pricing.exception.PricingAdjustmentAlreadyExistsException;
import com.arsaka.pricing.exception.PricingAdjustmentNotFoundException;
import com.arsaka.pricing.mapper.PricingAdjustmentMapper;
import com.arsaka.pricing.model.Fare;
import com.arsaka.pricing.model.PricingAdjustment;
import com.arsaka.pricing.repository.PricingAdjustmentRepository;
import com.arsaka.updaterequest.UpdatePricingAdjustmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingAdjustmentService {

    private final PricingAdjustmentRepository repository;
    private final FlightService flightService;
    private final FareService fareService;
    private final PricingAdjustmentMapper mapper;

    @Transactional
    public PricingAdjustmentResponse create(CreatePricingAdjustmentRequest request) {
        Flight flight = flightService.findById(request.flightId());

        Fare fare = fareService.findById(request.fareId());

        PricingAdjustment pricingAdjustment = repository.findByFlightAndFare(flight, fare).orElse(null);

        if (pricingAdjustment != null) {
            if (pricingAdjustment.isActive()) {
                log.debug("Pricing adjustment already exists exception | flight id={} | fare id={}", flight.getId(), fare.getId());
                throw new PricingAdjustmentAlreadyExistsException();
            }
            pricingAdjustment.setActive(true);
            return mapper.toResponse(pricingAdjustment);
        }

        pricingAdjustment = mapper.toEntity(request, flight, fare);

        PricingAdjustment saved = repository.save(pricingAdjustment);

        return mapper.toResponse(saved);
    }



    @Transactional
    public PricingAdjustmentResponse update(UUID id, UpdatePricingAdjustmentRequest request) {
        PricingAdjustment pricingAdjustment = findById(id);

        if(!pricingAdjustment.isActive()) {
            log.debug("Pricing adjustment not active | id={}", id);
            throw new PricingAdjustmentNotFoundException();
        }

        pricingAdjustment.setType(request.type());
        pricingAdjustment.setValue(request.value());

        return mapper.toResponse(pricingAdjustment);
    }

    public PricingAdjustment findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Pricing adjustment not found exception | id={}", id);
                    return new PricingAdjustmentNotFoundException();
                });
    }


    @Transactional
    public void delete(UUID id) {
        PricingAdjustment pricingAdjustment = findById(id);
        pricingAdjustment.setActive(false);
    }
}
