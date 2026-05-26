package com.arsaka.referencedata.controller;

import com.arsaka.controller.RouteApi;
import com.arsaka.search.request.RouteFilter;
import com.arsaka.search.response.RouteResponse;
import com.arsaka.referencedata.service.RouteAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteController implements RouteApi {

    private final RouteAdminService service;

    @Override
    public ResponseEntity<List<RouteResponse>> getRoutesWithoutActiveFlights() {
        List<RouteResponse> response = service.getActiveRoutesWithoutFlights();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<RouteResponse>> getRoutesWithConflictingStatuses(Instant date) {
        List<RouteResponse> response = service.getRoutesWithConflictingStatuses(date);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<RouteResponse>> searchRoutes(RouteFilter filter) {
        List<RouteResponse> response = service.searchRoutes(filter);
        return ResponseEntity.ok(response);
    }
}
