package com.arsaka.controller;

import com.arsaka.common.FlightStatus;
import com.arsaka.create.request.CreateFlightRequest;
import com.arsaka.create.response.FlightResponse;
import com.arsaka.search.response.AdminPage;
import com.arsaka.updaterequest.UpdateFlightRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin flights",
        description = "API for flights admin management: create, update, get list")
@RestController
@RequestMapping("/api/v1/admin/flights")
public interface FlightApi {

    @Operation(summary = "Create new Flight")
    @PostMapping
    ResponseEntity<FlightResponse> create(
            @Valid @RequestBody CreateFlightRequest request
    );

    @Operation(summary = "Update Flight")
    @PatchMapping("/{id}")
    ResponseEntity<FlightResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFlightRequest request
    );

    @Operation(summary = "Get flights list")
    @GetMapping
    ResponseEntity<AdminPage<FlightResponse>> listFlights(
            @RequestParam(required = false) UUID routeId,
            @RequestParam(required = false) FlightStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
