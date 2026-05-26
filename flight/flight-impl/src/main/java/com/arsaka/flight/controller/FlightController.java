package com.arsaka.flight.controller;

import com.arsaka.controller.FlightApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.common.FlightStatus;
import com.arsaka.create.request.CreateFlightRequest;
import com.arsaka.create.response.FlightResponse;
import com.arsaka.flight.service.FlightService;
import com.arsaka.updaterequest.UpdateFlightRequest;
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
