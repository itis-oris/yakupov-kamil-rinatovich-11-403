package com.arsaka.flight.controller;

import com.arsaka.controller.FlightInventoryApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.create.request.CreateFlightInventoryRequest;
import com.arsaka.create.response.FlightInventoryResponse;
import com.arsaka.flight.service.FlightInventoryService;
import com.arsaka.updaterequest.UpdateFlightInventoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FlightInventoryController implements FlightInventoryApi {

    private final FlightInventoryService service;

    @Override
    public ResponseEntity<FlightInventoryResponse> create(CreateFlightInventoryRequest request) {
        FlightInventoryResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<FlightInventoryResponse> update(UUID id, UpdateFlightInventoryRequest request) {
        FlightInventoryResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AdminPage<FlightInventoryResponse>> listInventories(UUID flightId, Integer page, Integer size) {
        AdminPage<FlightInventoryResponse> response = service.findInventories(flightId, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

}
