package com.arsaka.referencedata.service;

import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public AdminPage<AirlineResponse> findAirlines(
            String countryCode,
            Boolean active,
            AdminPageRequest pageReq
    ) {
        Specification<Airline> spec = Specification.where(null);

        if (countryCode != null && !countryCode.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("country").get("code"), countryCode.toUpperCase()));
        }
        if (active != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("active"), active));
        }

        Page<Airline> page = repository.findAll(
                spec,
                PageRequest.of(pageReq.page(), pageReq.size(), Sort.by("name"))
        );

        List<AirlineResponse> content = page.getContent()
                .stream()
                .map(airlineMapper::toResponse)
                .toList();

        return AdminPage.of(content, pageReq, page.getTotalElements());
    }
}