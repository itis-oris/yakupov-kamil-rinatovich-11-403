package com.arsaka.flightsearch.service;

import com.arsaka.flightsearch.exception.RouteNotFoundException;
import com.arsaka.flightsearch.model.Route;
import com.arsaka.flightsearch.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository repository;

    public Route findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id));
    }
}
