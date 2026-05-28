package com.oris.flight.service;

import com.oris.flight.dto.FlightSearchDto;
import com.oris.search.response.dto.FlightsSearchFilter;
import com.oris.flight.repository.FlightSearchFilterRepository;
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
