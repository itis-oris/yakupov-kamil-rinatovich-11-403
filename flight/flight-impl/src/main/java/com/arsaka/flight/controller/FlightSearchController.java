package com.arsaka.flight.controller;

import com.arsaka.controller.FlightSearchApi;
import com.arsaka.flight.service.FlightSearchOrchestratorService;
import com.arsaka.search.request.FlightWithFaresRequest;
import com.arsaka.search.request.FlightsSearchRequest;
import com.arsaka.search.response.FlightWithFaresResponse;
import com.arsaka.search.response.FlightsSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class  FlightSearchController implements FlightSearchApi {

    private final FlightSearchOrchestratorService service;

    @Override
    public ResponseEntity<FlightsSearchResponse> getFlights(FlightsSearchRequest request) {
        FlightsSearchResponse response = service.getFlights(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FlightWithFaresResponse> getFlightWithFares(UUID flightId, FlightWithFaresRequest request) {
        FlightWithFaresResponse response = service.getFlightWithFares(flightId, request);
        return ResponseEntity.ok(response);
    }

}
