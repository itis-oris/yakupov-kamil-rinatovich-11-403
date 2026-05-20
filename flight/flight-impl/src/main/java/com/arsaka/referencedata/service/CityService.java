package com.arsaka.referencedata.service;

import com.arsaka.referencedata.dto.CityDto;
import com.arsaka.referencedata.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository repository;

    @Transactional
    public List<CityDto> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(c -> new CityDto(c.getCode(), c.getName()))
                .toList();
    }

}
