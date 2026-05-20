package com.arsaka.referencedata.service;

import com.arsaka.referencedata.dto.CountryDto;
import com.arsaka.referencedata.exception.CountryNotFoundException;
import com.arsaka.referencedata.model.Country;
import com.arsaka.referencedata.repository.CountryCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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
    public List<CountryDto> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(c -> new CountryDto(c.getCode(), c.getName()))
                .toList();
    }

}
