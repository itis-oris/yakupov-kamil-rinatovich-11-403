package com.arsaka.flightsearch.service;

import com.arsaka.flightsearch.dto.FlightSearchDto;
import com.arsaka.search.response.dto.FlightsSearchFilter;
import com.arsaka.flightsearch.repository.FlightSearchFilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightSearchFilterService {

    private final FlightSearchFilterRepository repository;

    public FlightsSearchFilter getFilterResponse(FlightSearchDto dto) {
        return repository.findFilter(dto);
    }

}
