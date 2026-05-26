package com.arsaka.controller;

import com.arsaka.create.request.CreateAirlineRequest;
import com.arsaka.create.response.AirlineResponse;
import com.arsaka.search.response.AdminPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin airlines",
        description = "API for airlines admin management: create, delete, get list")
@RestController
@RequestMapping("/api/v1/admin/flights/airlines")
@Validated
public interface AirlineApi {

    @Operation(summary = "Create new Airline")
    @PostMapping
    ResponseEntity<AirlineResponse> create(
            @Valid @RequestBody CreateAirlineRequest request
    );

    @Operation(summary = "Delete Airline")
    @DeleteMapping("/{code}")
    ResponseEntity<Void> delete(
            @PathVariable
            @NotBlank
            @Pattern(regexp = "^[A-Z0-9]{2}$")
            String code
    );

    @Operation(summary = "Get airlines list")
    @GetMapping
    ResponseEntity<AdminPage<AirlineResponse>> listAirlines(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
