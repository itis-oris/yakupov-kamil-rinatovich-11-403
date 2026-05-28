package com.oris.controller;

import com.oris.search.request.RouteFilter;
import com.oris.search.response.RouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "Admin routes",
        description = "API for routes admin get management: without-flights, conflicting, list by filter")
@RestController
@RequestMapping("/api/v1/admin/flights/routes")
public interface RouteApi {

    @Operation(summary = "Get routes without flights")
    @GetMapping("/without-flights")
    ResponseEntity<List<RouteResponse>> getRoutesWithoutActiveFlights();

    @Operation(summary = "Get conflicting routes")
    @GetMapping("/conflicting")
    ResponseEntity<List<RouteResponse>> getRoutesWithConflictingStatuses(
            @RequestParam Instant date
    );

    @Operation(summary = "Get list routes by filter")
    @GetMapping
    ResponseEntity<List<RouteResponse>> searchRoutes(
            @ModelAttribute RouteFilter filter
    );
}
