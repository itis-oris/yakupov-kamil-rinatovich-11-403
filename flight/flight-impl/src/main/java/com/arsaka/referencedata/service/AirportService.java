package com.arsaka.referencedata.service;

import com.arsaka.config.CacheName;
import com.arsaka.referencedata.dto.AirportDto;
import com.arsaka.referencedata.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@CacheConfig(cacheNames = CacheName.AIRPORT_LIST)
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository repository;

    @Transactional
    @Cacheable
    public List<AirportDto> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(c -> new AirportDto(c.getCode(), c.getName()))
                .toList();
    }

}
