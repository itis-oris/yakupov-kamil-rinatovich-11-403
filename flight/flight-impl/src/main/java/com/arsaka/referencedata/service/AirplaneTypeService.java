package com.arsaka.referencedata.service;

import com.arsaka.referencedata.dto.AirplaneTypeDto;
import com.arsaka.referencedata.exception.AirplaneTypeNotFoundException;
import com.arsaka.referencedata.model.AirplaneType;
import com.arsaka.referencedata.repository.AirplaneTypeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirplaneTypeService {

    private final AirplaneTypeCommandRepository repository;
//    private final AirplaneTypeMapper mapper;
//
//    @Transactional
//    public AirplaneTypeResponse create(CreateAirplaneTypeRequest request) {
//        if(repository.existsById(request.code())) {
//            throw new AirplaneTypeAlreadyExistsException(request.code());
//        }
//
//
//        AirplaneType airplaneType = mapper.toEntity(request);
//
//        AirplaneType saved = repository.save(airplaneType);
//
//        return mapper.toResponse(saved);
//    }

    public AirplaneType findByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> {
                    log.debug("Airplane type not found exception | airline type code={}", code);
                    return new AirplaneTypeNotFoundException();
                });
    }

    @Transactional
    public List<AirplaneTypeDto> findAll() {
        return repository.findAll(Sort.by("manufacturer")).stream()
                .map(a -> new AirplaneTypeDto(a.getCode(), a.getManufacturer(), a.getModel(), a.getTotalSeats()))
                .toList();
    }

}