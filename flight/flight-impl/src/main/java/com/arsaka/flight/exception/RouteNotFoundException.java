package com.arsaka.flight.exception;

import com.arsaka.exception.NotFoundException;

public class RouteNotFoundException extends NotFoundException {
    public RouteNotFoundException() {
        super("Route not found");
    }
}
