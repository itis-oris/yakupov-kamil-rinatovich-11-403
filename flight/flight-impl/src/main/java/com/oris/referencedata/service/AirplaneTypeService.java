package com.oris.referencedata.service;

import com.oris.config.CacheName;
import com.oris.referencedata.dto.AirplaneTypeDto;
import com.oris.referencedata.exception.AirplaneTypeNotFoundException;
import com.oris.referencedata.model.AirplaneType;
import com.oris.referencedata.repository.AirplaneTypeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheName.AIRPLANE_TYPE_LIST)
@Slf4j
public class AirplaneTypeService {

    private final AirplaneTypeCommandRepository repository;

    public AirplaneType findByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> {
                    log.debug("Airplane type not found exception | airline type code={}", code);
                    return new AirplaneTypeNotFoundException();
                });
    }

    @Transactional
    @Cacheable
    public List<AirplaneTypeDto> findAll() {
        return repository.findAll(Sort.by("manufacturer")).stream()
                .map(a -> new AirplaneTypeDto(a.getCode(), a.getManufacturer(), a.getModel(), a.getTotalSeats()))
                .toList();
    }

}