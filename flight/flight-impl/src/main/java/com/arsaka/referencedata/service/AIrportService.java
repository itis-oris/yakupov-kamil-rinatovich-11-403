package com.arsaka.referencedata.service;

import com.arsaka.referencedata.dto.AirportDto;
import com.arsaka.referencedata.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AIrportService {

    private final AirportRepository repository;

    @Transactional
    public List<AirportDto> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(c -> new AirportDto(c.getCode(), c.getName()))
                .toList();
    }

}
