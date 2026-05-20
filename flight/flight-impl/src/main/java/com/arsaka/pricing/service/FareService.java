package com.arsaka.pricing.service;

import com.arsaka.common.CabinClass;
import com.arsaka.create.request.CreateFareRequest;
import com.arsaka.create.response.FareResponse;
import com.arsaka.pricing.mapper.FareMapper;
import com.arsaka.pricing.model.Fare;
import com.arsaka.pricing.repository.FareCommandRepository;
import com.arsaka.referencedata.model.Airline;
import com.arsaka.referencedata.service.AirlineService;
import com.arsaka.search.response.dto.FlightFare;
import com.arsaka.pricing.exception.FareNotFoundException;
import com.arsaka.pricing.repository.FareQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
