package com.arsaka.flight.controller;

import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.create.request.CreateFlightInventoryRequest;
import com.arsaka.create.response.FlightInventoryResponse;
import com.arsaka.flight.service.FlightInventoryService;
import com.arsaka.updaterequest.UpdateFlightInventoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/flights/inventories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FlightInventoryController {

    private final FlightInventoryService service;

    @PostMapping
    public ResponseEntity<FlightInventoryResponse> create(
            @Valid @RequestBody CreateFlightInventoryRequest request
    ) {
        FlightInventoryResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FlightInventoryResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFlightInventoryRequest request
    ) {
        FlightInventoryResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<AdminPage<FlightInventoryResponse>> listInventories(
            @RequestParam(required = false) UUID flightId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        AdminPage<FlightInventoryResponse> response = service.findInventories(flightId, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

}
