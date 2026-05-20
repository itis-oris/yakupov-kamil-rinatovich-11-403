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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
                log.debug("Airplane with this number already exists exception | airplane number={}", request.number());
                throw new AirplaneAlreadyExistsException();
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
                .orElseThrow(() -> {
                    log.debug("Airplane not found exception | airline id={}", id);
                    return new AirplaneNotFoundException();
                });
    }

    @Transactional
    public void delete(UUID id) {
        Airplane airplane = findById(id);
        airplane.setActive(false);
    }
}