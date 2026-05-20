package com.arsaka.referencedata.controller;

import com.arsaka.search.request.RouteFilter;
import com.arsaka.search.response.RouteResponse;
import com.arsaka.referencedata.service.RouteAdminService;
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

    @GetMapping
    public ResponseEntity<List<RouteResponse>> searchRoutes(
            @ModelAttribute RouteFilter filter
    ) {
        List<RouteResponse> response = service.searchRoutes(filter);
        return ResponseEntity.ok(response);
    }
}
