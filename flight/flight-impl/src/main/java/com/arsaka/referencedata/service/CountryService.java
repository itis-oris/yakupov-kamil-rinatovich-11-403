package com.arsaka.referencedata.service;

import com.arsaka.referencedata.exception.CountryNotFoundException;
import com.arsaka.referencedata.model.Country;
import com.arsaka.referencedata.repository.CountryCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
