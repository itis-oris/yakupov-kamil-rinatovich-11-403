package com.oris.referencedata.service;

import com.oris.config.CacheName;
import com.oris.referencedata.dto.CityDto;
import com.oris.referencedata.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@CacheConfig(cacheNames = CacheName.CITY_LIST)
@RequiredArgsConstructor
public class CityService {

    private final CityRepository repository;

    @Transactional
    @Cacheable
    public List<CityDto> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(c -> new CityDto(c.getCode(), c.getName()))
                .toList();
    }

}
