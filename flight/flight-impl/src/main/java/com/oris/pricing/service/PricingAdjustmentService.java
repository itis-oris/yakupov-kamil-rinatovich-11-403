package com.oris.pricing.service;

import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.create.request.CreatePricingAdjustmentRequest;
import com.oris.create.response.PricingAdjustmentResponse;
import com.oris.flight.model.Flight;
import com.oris.flight.service.FlightService;
import com.oris.pricing.exception.PricingAdjustmentAlreadyExistsException;
import com.oris.pricing.exception.PricingAdjustmentNotFoundException;
import com.oris.pricing.mapper.PricingAdjustmentMapper;
import com.oris.pricing.model.Fare;
import com.oris.pricing.model.PricingAdjustment;
import com.oris.pricing.repository.PricingAdjustmentRepository;
import com.oris.updaterequest.UpdatePricingAdjustmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public AdminPage<PricingAdjustmentResponse> findAdjustments(
            UUID flightId,
            UUID fareId,
            AdminPageRequest pageReq
    ) {
        Specification<PricingAdjustment> spec = Specification.where(null);

        if (flightId != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("flight").get("id"), flightId));
        }
        if (fareId != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("fare").get("id"), fareId));
        }

        Page<PricingAdjustment> page = repository.findAll(
                spec,
                PageRequest.of(pageReq.page(), pageReq.size(), Sort.by("flight"))
        );

        List<PricingAdjustmentResponse> content = page.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return AdminPage.of(content, pageReq, page.getTotalElements());
    }
}
