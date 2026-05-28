package com.oris.controller;

import com.oris.search.request.FlightWithFaresRequest;
import com.oris.search.request.FlightsSearchRequest;
import com.oris.search.response.FlightWithFaresResponse;
import com.oris.search.response.FlightsSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Flights searching",
        description = "API for searching flights: get flights list, get info by one flight")
@RestController
@RequestMapping("/api/v1/flights")
public interface FlightSearchApi {

    @Operation(summary = "Get flights list with base info")
    @PostMapping("/search")
    ResponseEntity<FlightsSearchResponse> getFlights(
            @Valid @RequestBody FlightsSearchRequest request
    );

    @Operation(summary = "Get info by one flight with fares")
    @PostMapping("/{flightId}/fares")
    ResponseEntity<FlightWithFaresResponse> getFlightWithFares(
            @PathVariable UUID flightId,
            @Valid @RequestBody FlightWithFaresRequest request
    );
}
