package com.arsaka.referencedata.service;

import com.arsaka.referencedata.exception.CountryNotFoundException;
import com.arsaka.referencedata.model.Country;
import com.arsaka.referencedata.repository.CountryCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryCommandRepository repository;

    public Country findByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new CountryNotFoundException(code));
    }

}
