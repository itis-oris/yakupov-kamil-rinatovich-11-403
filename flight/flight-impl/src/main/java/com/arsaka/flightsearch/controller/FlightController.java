package com.arsaka.flightsearch.controller;

import com.arsaka.create.request.CreateFlightRequest;
import com.arsaka.create.response.FlightResponse;
import com.arsaka.flightsearch.service.FlightService;
import com.arsaka.updaterequest.UpdateFlightRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/flights")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FlightController {

    private final FlightService service;

    @PostMapping
    public ResponseEntity<FlightResponse> create(
            @Valid @RequestBody CreateFlightRequest request
    ) {
        FlightResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FlightResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFlightRequest request
    ) {
        FlightResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

}
