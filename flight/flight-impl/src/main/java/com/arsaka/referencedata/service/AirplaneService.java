package com.arsaka.referencedata.service;

import com.arsaka.create.request.CreateAirplaneRequest;
import com.arsaka.create.response.AirplaneResponse;
import com.arsaka.referencedata.mapper.AirplaneMapper;
import com.arsaka.referencedata.exception.AirplaneAlreadyExistsException;
import com.arsaka.referencedata.exception.AirplaneNotFoundException;
import com.arsaka.referencedata.model.Airline;
import com.arsaka.referencedata.model.Airplane;
import com.arsaka.referencedata.model.AirplaneType;
import com.arsaka.referencedata.repository.AirplaneCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AirplaneService {

    private final AirplaneCommandRepository repository;
    private final AirlineService airlineService;
    private final AirplaneTypeService airplaneTypeService;
    private final AirplaneMapper mapper;

    @Transactional
    public AirplaneResponse create(CreateAirplaneRequest request) {
        Airplane airplane = repository.findByNumber(request.number()).orElse(null);

        if (airplane != null) {
            if (airplane.isActive()) {
                throw new AirplaneAlreadyExistsException(request.number());
            }
            airplane.setActive(true);
            return mapper.toResponse(airplane);
        }

        AirplaneType airplaneType = airplaneTypeService.findByCode(request.airplaneTypeCode());

        Airline airline = airlineService.findByCode(request.airlineCode());

        airplane = mapper.toEntity(request, airplaneType, airline);

        Airplane saved = repository.save(airplane);

        return mapper.toResponse(saved);
    }

    public Airplane findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));
    }

    @Transactional
    public void delete(UUID id) {
        Airplane airplane = repository.findById(id)
                .orElseThrow(() -> new AirplaneNotFoundException(id));

        airplane.setActive(false);
    }
}