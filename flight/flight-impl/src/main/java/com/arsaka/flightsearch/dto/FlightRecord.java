package com.arsaka.flightsearch.dto;

import com.arsaka.common.CabinClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class FlightRecord {
    private UUID flightId;

    private String airlineCode;
    private String airlineName;

    private String departureAirportCode;
    private String departureAirportName;
    private String departureAirportCityName;
    private String departureAirportCountryName;

    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCityName;
    private String arrivalAirportCountryName;

    private String airplaneTypeCode;
    private String airplaneTypeManufacturer;
    private String airplaneTypeModel;

    private Instant scheduledDeparture;
    private Instant scheduledArrival;

    private CabinClass cabinClass;
    private Integer totalSeats;
    private Integer availableSeats;
    private Integer heldSeats;
    private BigDecimal price;
}
