package com.arsaka.controller;

import com.arsaka.create.request.CreateFlightInventoryRequest;
import com.arsaka.create.response.FlightInventoryResponse;
import com.arsaka.search.response.AdminPage;
import com.arsaka.updaterequest.UpdateFlightInventoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin flight inventories",
        description = "API for flight inventories admin management: create, update, get list")
@RestController
@RequestMapping("/api/v1/admin/flights/inventories")
public interface FlightInventoryApi {

    @Operation(summary = "Create new flight inventory")
    @PostMapping
    ResponseEntity<FlightInventoryResponse> create(
            @Valid @RequestBody CreateFlightInventoryRequest request
    );

    @Operation(summary = "Update flight inventory")
    @PatchMapping("/{id}")
    ResponseEntity<FlightInventoryResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFlightInventoryRequest request
    );

    @Operation(summary = "Get flight inventories list")
    @GetMapping
    ResponseEntity<AdminPage<FlightInventoryResponse>> listInventories(
            @RequestParam(required = false) UUID flightId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
