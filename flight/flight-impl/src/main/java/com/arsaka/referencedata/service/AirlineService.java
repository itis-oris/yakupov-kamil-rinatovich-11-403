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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AirlineService {

    private final AirlineCommandRepository repository;
    private final CountryService countryService;
    private final AirlineMapper airlineMapper;

    @Transactional
    public AirlineResponse create(CreateAirlineRequest request) {
        Airline airline = repository.findById(request.code()).orElse(null);

        if (airline != null) {
            if (airline.isActive()) {
                throw new AirlineAlreadyExistsException(request.code());
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
                .orElseThrow(() -> new AirlineNotFoundException(code));
    }

    @Transactional
    public void delete(String code) {
        Airline airline = repository.findById(code)
                .orElseThrow(() -> new AirlineNotFoundException(code));

        airline.setActive(false);
    }
}