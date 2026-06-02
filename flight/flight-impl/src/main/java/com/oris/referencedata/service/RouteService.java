package com.oris.referencedata.service;

import com.oris.referencedata.exception.RouteNotFoundException;
import com.oris.referencedata.model.Route;
import com.oris.referencedata.repository.RouteCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final RouteCommandRepository repository;

    public Route findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.debug("route not found exception | route id={}", id);
                    return new RouteNotFoundException();
                });
    }
}
