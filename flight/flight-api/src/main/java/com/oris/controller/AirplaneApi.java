package com.oris.controller;

import com.oris.create.request.CreateAirplaneRequest;
import com.oris.create.response.AirplaneResponse;
import com.oris.search.response.AdminPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin airplanes",
        description = "API for airplanes admin management: create, delete, get list")
@RestController
@RequestMapping("/api/v1/admin/flights/airplanes")
public interface AirplaneApi {

    @Operation(summary = "Create new Airplane")
    @PostMapping
    ResponseEntity<AirplaneResponse> create(
            @Valid @RequestBody CreateAirplaneRequest request
    );

    @Operation(summary = "Delete Airplane")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable UUID id
    );

    @Operation(summary = "Get airplanes list")
    @GetMapping
    ResponseEntity<AdminPage<AirplaneResponse>> listAirplanes(
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) String airplaneTypeCode,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
