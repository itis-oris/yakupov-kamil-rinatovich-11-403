package com.arsaka.flightsearch.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class RouteNotFoundException extends NotFoundException {
    public RouteNotFoundException(UUID id) {
        super("Route not found | route id=%s".formatted(id));
    }
}
