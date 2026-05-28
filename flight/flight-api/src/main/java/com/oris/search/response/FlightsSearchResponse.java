package com.oris.search.response;

import com.oris.search.response.dto.FlightSearch;
import com.oris.search.response.dto.FlightsSearchFilter;

import java.util.List;

public record FlightsSearchResponse(
        List<FlightSearch> flights,
        FlightsSearchFilter filter,
        String nextCursor
) {
}
