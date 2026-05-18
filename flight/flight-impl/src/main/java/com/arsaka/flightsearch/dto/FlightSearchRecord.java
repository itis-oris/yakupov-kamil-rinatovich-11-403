package com.arsaka.flightsearch.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class FlightSearchRecord {
    private UUID flightId;

    private String departureAirportCode;
    private String departureAirportName;
    private String departureAirportCityName;
    private String departureAirportCountryName;

    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCityName;
    private String arrivalAirportCountryName;

    private Instant scheduledDeparture;
    private Instant scheduledArrival;

    private Integer totalSeats;
    private Integer availableSeats;
    private Integer heldSeats;
    private BigDecimal price;
}