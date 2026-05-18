package com.arsaka.referencedata.controller;

import com.arsaka.search.request.RouteFilter;
import com.arsaka.search.response.RouteResponse;
import com.arsaka.referencedata.service.RouteAdminService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/flights/routes")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RouteAdminController {

    private final RouteAdminService service;

    @GetMapping("/by-airline")
    public ResponseEntity<List<RouteResponse>> getByAirline(
            @RequestParam @Pattern(regexp = "^[A-Z0-9]{2}$") String airlineCode,
            @RequestParam(defaultValue = "true") boolean isActive
    ) {
        List<RouteResponse> response = service.getByAirline(airlineCode, isActive);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/without-flights")
    public ResponseEntity<List<RouteResponse>> getRoutesWithoutActiveFlights() {
        List<RouteResponse> response = service.getActiveRoutesWithoutFlights();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/conflicting")
    public ResponseEntity<List<RouteResponse>> getRoutesWithConflictingStatuses(
            @RequestParam Instant date
    ) {
        List<RouteResponse> response = service.getRoutesWithConflictingStatuses(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RouteResponse>> searchRoutes(
            @ModelAttribute RouteFilter filter
    ) {
        List<RouteResponse> response = service.searchRoutes(filter);
        return ResponseEntity.ok(response);
    }
}
