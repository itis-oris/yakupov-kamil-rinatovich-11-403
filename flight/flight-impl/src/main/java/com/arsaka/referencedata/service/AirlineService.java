package com.arsaka.referencedata.service;

import com.arsaka.create.request.CreateAirlineRequest;
import com.arsaka.create.response.AirlineResponse;
import com.arsaka.referencedata.mapper.AirlineMapper;
import com.arsaka.referencedata.exception.AirlineAlreadyExistsException;
import com.arsaka.referencedata.exception.AirlineNotFoundException;
import com.arsaka.referencedata.model.Airline;
import com.arsaka.referencedata.model.Country;
import com.arsaka.referencedata.repository.AirlineCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirlineService {

    private final AirlineCommandRepository repository;
    private final CountryService countryService;
    private final AirlineMapper airlineMapper;

    @Transactional
    public AirlineResponse create(CreateAirlineRequest request) {
        Airline airline = repository.findById(request.code()).orElse(null);

        if (airline != null) {
            if (airline.isActive()) {
                log.debug("Airline with this code already exists exception | airline code={}", request.code());
                throw new AirlineAlreadyExistsException();
            }
            airline.setActive(true);
            return airlineMapper.toResponse(airline);
        }

        Country country = countryService.findByCode(request.countryCode());

        airline = airlineMapper.toEntity(request, country);

        Airline saved = repository.save(airline);

        return airlineMapper.toResponse(saved);
    }

    public Airline findByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> {
                    log.debug("Airline not found exception | airline code={}", code);
                    return new AirlineNotFoundException();
                });
    }

    @Transactional
    public void delete(String code) {
        Airline airline = findByCode(code);
        airline.setActive(false);
    }
}