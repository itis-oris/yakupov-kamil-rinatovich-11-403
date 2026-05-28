package com.oris.referencedata.service;

import com.oris.config.CacheName;
import com.oris.referencedata.dto.CountryDto;
import com.oris.referencedata.exception.CountryNotFoundException;
import com.oris.referencedata.model.Country;
import com.oris.referencedata.repository.CountryCommandRepository;
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
@CacheConfig(cacheNames = CacheName.COUNTRY_LIST)
@Slf4j
public class CountryService {

    private final CountryCommandRepository repository;

    public Country findByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> {
                    log.debug("Country not found exception | country code={}", code);
                    return new CountryNotFoundException();
                });
    }

    @Transactional
    @Cacheable
    public List<CountryDto> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(c -> new CountryDto(c.getCode(), c.getName()))
                .toList();
    }

}
