package com.arsaka.referencedata.service;

import com.arsaka.referencedata.exception.AirplaneTypeNotFoundException;
import com.arsaka.referencedata.model.AirplaneType;
import com.arsaka.referencedata.repository.AirplaneTypeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
                .orElseThrow(() -> new AirplaneTypeNotFoundException(code));
    }

}