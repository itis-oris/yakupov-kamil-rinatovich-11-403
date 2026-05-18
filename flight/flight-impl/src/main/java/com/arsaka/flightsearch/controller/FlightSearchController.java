package com.arsaka.flightsearch.controller;

import com.arsaka.flightsearch.service.FlightSearchOrchestratorService;
import com.arsaka.search.request.FlightRequest;
import com.arsaka.search.request.FlightWithFaresRequest;
import com.arsaka.search.request.FlightsSearchRequest;
import com.arsaka.search.response.FlightSearchResponse;
import com.arsaka.search.response.FlightWithFaresResponse;
import com.arsaka.search.response.FlightsSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Slf4j
public class FlightSearchController {

    private final FlightSearchOrchestratorService service;

    @PostMapping("/search")
    public ResponseEntity<FlightsSearchResponse> getFlights(
            @Valid @RequestBody FlightsSearchRequest request
    ) {
        FlightsSearchResponse response = service.getFlights(request);
        log.info("flights search response | {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{flightId}/fares")
    public ResponseEntity<FlightWithFaresResponse> getFlightWithFares(
            @PathVariable UUID flightId,
            @Valid @RequestBody FlightWithFaresRequest request
    ) {
        FlightWithFaresResponse response = service.getFlightWithFares(flightId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<FlightSearchResponse> getFlight(
            @PathVariable UUID flightId,
            @Valid @ModelAttribute FlightRequest request
    ) {
        FlightSearchResponse response = service.getFlight(flightId, request);
        return ResponseEntity.ok(response);
    }

}
