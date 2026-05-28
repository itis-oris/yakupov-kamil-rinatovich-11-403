package com.oris.pricing.service;

import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.common.CabinClass;
import com.oris.create.request.CreateFareRequest;
import com.oris.create.response.FareResponse;
import com.oris.pricing.mapper.FareMapper;
import com.oris.pricing.model.Fare;
import com.oris.pricing.repository.FareCommandRepository;
import com.oris.referencedata.model.Airline;
import com.oris.referencedata.service.AirlineService;
import com.oris.search.response.dto.FlightFare;
import com.oris.pricing.exception.FareNotFoundException;
import com.oris.pricing.repository.FareQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FareService {

    private final FareQueryRepository queryRepository;
    private final FareCommandRepository commandRepository;
    private final AirlineService airlineService;
    private final FareMapper mapper;

    public Set<FlightFare> getFares(String airlineCode, CabinClass cabinClass) {
        return queryRepository.getFares(airlineCode, cabinClass);
    }

    public FlightFare getFare(UUID fareId) {
        FlightFare fare = queryRepository.getFare(fareId);

        if(fare == null) {
            log.debug("Fare not found exception | fare id={}", fareId);
            throw new FareNotFoundException();
        }

        return fare;
    }

    public CabinClass getCabinClass(UUID fareId) {
        CabinClass cabinClass = queryRepository.getCabinClass(fareId);

        if(cabinClass == null) {
            log.debug("Fare not found exception | fare id={}", fareId);
            throw new FareNotFoundException();
        }

        return cabinClass;
    }

    public Fare findById(UUID id) {
        return commandRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Fare not found exception | fare id={}", id);
                    return new FareNotFoundException();
                });
    }

    @Transactional
    public FareResponse create(CreateFareRequest request) {
        Airline airline = airlineService.findByCode(request.airlineCode());

        Fare fare = mapper.toEntity(request, airline);

        Fare saved = commandRepository.save(fare);

        return mapper.toResponse(saved);
    }


    @Transactional
    public void delete(UUID id) {
        Fare fare = findById(id);
        fare.setActive(false);
    }

    @Transactional
    public AdminPage<FareResponse> findFares(
            String airlineCode,
            CabinClass cabinClass,
            AdminPageRequest pageReq
    ) {
        Specification<Fare> spec = Specification.where(null);

        if (airlineCode != null && !airlineCode.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("airline").get("code"), airlineCode.toUpperCase()));
        }
        if (cabinClass != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("cabinClass"), cabinClass));
        }

        Page<Fare> page = commandRepository.findAll(
                spec,
                PageRequest.of(pageReq.page(), pageReq.size(), Sort.by("airline"))
        );

        List<FareResponse> content = page.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return AdminPage.of(content, pageReq, page.getTotalElements());
    }
}
