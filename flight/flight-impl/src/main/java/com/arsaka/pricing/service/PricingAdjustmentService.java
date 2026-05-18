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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
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
                throw new PricingAdjustmentAlreadyExistsException(flight.getId(), fare.getId());
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
        PricingAdjustment pricingAdjustment = repository.findById(id)
                .orElseThrow(() -> new PricingAdjustmentNotFoundException(id));

        if(!pricingAdjustment.isActive()) {
            throw new PricingAdjustmentNotFoundException(id);
        }

        pricingAdjustment.setType(request.type());
        pricingAdjustment.setValue(request.value());

        return mapper.toResponse(pricingAdjustment);
    }


    @Transactional
    public void delete(UUID id) {
        PricingAdjustment pricingAdjustment = repository.findById(id)
                .orElseThrow(() -> new PricingAdjustmentNotFoundException(id));

        pricingAdjustment.setActive(false);
    }
}
