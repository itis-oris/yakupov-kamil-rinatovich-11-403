package com.arsaka.referencedata.service;

import com.arsaka.common.FlightStatus;
import com.arsaka.referencedata.mapper.RouteMapper;
import com.arsaka.search.request.RouteFilter;
import com.arsaka.search.response.RouteResponse;
import com.arsaka.referencedata.repository.RouteAdminRepository;
import com.arsaka.referencedata.repository.RouteAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteAdminService {

    private final RouteAdminRepository routeAdminRepository;
    private final RouteAnalyticsRepository routeAnalyticsRepository;
    private final RouteMapper routeMapper;

    @Transactional
    public List<RouteResponse> getActiveRoutesWithoutFlights() {
        return routeAdminRepository
                .findActiveRoutesWithoutActiveFlights(List.of(
                        FlightStatus.SCHEDULED,
                        FlightStatus.ON_TIME,
                        FlightStatus.DELAYED,
                        FlightStatus.BOARDING,
                        FlightStatus.DEPARTED
                ))
                .stream()
                .map(routeMapper::toResponse)
                .toList();
    }

    @Transactional
    public List<RouteResponse> getRoutesWithConflictingStatuses(Instant date) {
        return routeAdminRepository
                .findRoutesWithConflictingFlightStatuses(
                        FlightStatus.SCHEDULED,
                        FlightStatus.CANCELLED,
                        date
                )
                .stream()
                .map(routeMapper::toResponse)
                .toList();
    }

    @Transactional
    public List<RouteResponse> searchRoutes(RouteFilter filter) {
        return routeAnalyticsRepository
                .findRoutesWithFilter(filter)
                .stream()
                .map(routeMapper::toResponse)
                .toList();
    }
}
