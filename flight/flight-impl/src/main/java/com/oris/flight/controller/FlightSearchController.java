package com.oris.flight.controller;

import com.oris.controller.FlightSearchApi;
import com.oris.flight.service.FlightSearchOrchestratorService;
import com.oris.search.request.FlightWithFaresRequest;
import com.oris.search.request.FlightsSearchRequest;
import com.oris.search.response.FlightWithFaresResponse;
import com.oris.search.response.FlightsSearchResponse;
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
