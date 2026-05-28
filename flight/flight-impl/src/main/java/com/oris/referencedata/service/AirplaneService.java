package com.oris.referencedata.service;

import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.create.request.CreateAirplaneRequest;
import com.oris.create.response.AirplaneResponse;
import com.oris.referencedata.mapper.AirplaneMapper;
import com.oris.referencedata.exception.AirplaneAlreadyExistsException;
import com.oris.referencedata.exception.AirplaneNotFoundException;
import com.oris.referencedata.model.Airline;
import com.oris.referencedata.model.Airplane;
import com.oris.referencedata.model.AirplaneType;
import com.oris.referencedata.repository.AirplaneCommandRepository;
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

    @Transactional
    public AdminPage<AirplaneResponse> findAirplanes(
            String airlineCode,
            String airplaneTypeCode,
            AdminPageRequest pageReq
    ) {
        Specification<Airplane> spec = Specification.where(null);

        if (airlineCode != null && !airlineCode.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("airline").get("code"), airlineCode.toUpperCase()));
        }
        if (airplaneTypeCode != null && !airplaneTypeCode.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("airplaneType").get("code"), airplaneTypeCode.toUpperCase()));
        }

        Page<Airplane> page = repository.findAll(
                spec,
                PageRequest.of(pageReq.page(), pageReq.size(), Sort.by("number"))
        );

        List<AirplaneResponse> content = page.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return AdminPage.of(content, pageReq, page.getTotalElements());
    }
}