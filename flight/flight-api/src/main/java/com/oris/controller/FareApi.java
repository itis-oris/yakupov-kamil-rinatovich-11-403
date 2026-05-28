package com.oris.controller;

import com.oris.common.CabinClass;
import com.oris.create.request.CreateFareRequest;
import com.oris.create.response.FareResponse;
import com.oris.search.response.AdminPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin fares",
        description = "API for fares admin management: create, delete, get list")
@RestController
@RequestMapping("/api/v1/admin/flights/fares")
public interface FareApi {

    @Operation(summary = "Create new Fare")
    @PostMapping
    ResponseEntity<FareResponse> create(
            @Valid @RequestBody CreateFareRequest request
    );

    @Operation(summary = "Delete Fare")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable UUID id
    );

    @Operation(summary = "Get fares list")
    @GetMapping
    ResponseEntity<AdminPage<FareResponse>> listFares(
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) CabinClass cabinClass,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
