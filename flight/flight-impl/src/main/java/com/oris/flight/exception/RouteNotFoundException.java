package com.oris.flight.exception;

import com.oris.exception.NotFoundException;

public class RouteNotFoundException extends NotFoundException {
    public RouteNotFoundException() {
        super("Route not found");
    }
}
