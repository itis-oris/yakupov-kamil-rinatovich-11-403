package com.arsaka.search.response;

import com.arsaka.search.response.dto.FlightSearch;
import com.arsaka.search.response.dto.FlightsSearchFilter;

import java.util.List;

public record FlightsSearchResponse(
        List<FlightSearch> flights,
        FlightsSearchFilter filter,
        String nextCursor
) {
}
