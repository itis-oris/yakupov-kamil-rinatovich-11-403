package com.oris.flight.controller;

import com.oris.controller.FlightApi;
import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.common.FlightStatus;
import com.oris.create.request.CreateFlightRequest;
import com.oris.create.response.FlightResponse;
import com.oris.flight.service.FlightService;
import com.oris.updaterequest.UpdateFlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FlightController implements FlightApi {

    private final FlightService service;

    @Override
    public ResponseEntity<FlightResponse> create(CreateFlightRequest request) {
        FlightResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<FlightResponse> update(UUID id, UpdateFlightRequest request) {
        FlightResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AdminPage<FlightResponse>> listFlights(UUID routeId, FlightStatus status, Integer page, Integer size) {
        AdminPage<FlightResponse> response = service.findFlights(routeId, status, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

}
