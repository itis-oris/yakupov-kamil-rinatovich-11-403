package com.arsaka.booking.dto;

import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
import com.arsaka.event.dto.DocumentType;
import com.arsaka.event.dto.PassengerGender;
import com.arsaka.search.request.dto.TicketStatus;
import com.arsaka.search.response.dto.SeatType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TicketWithDetailsSearchRecord {
    private UUID id;
    private UUID accountId;
    private TicketStatus status;
    private UUID bookingId;

    private String passengerFirstName;
    private String passengerLastName;
    private LocalDate passengerDateOfBirth;
    private PassengerGender passengerGender;

    private DocumentType documentType;
    private String documentNumber;
    private String documentCountryCode;
    private PassengerType passengerType;
    private Instant createdAt;

    private UUID flightId;
    private String departureAirportCode;
    private String departureAirportName;
    private String departureAirportCityName;
    private String departureAirportCountryName;
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCityName;
    private String arrivalAirportCountryName;
    private String scheduledDeparture;
    private String scheduledArrival;

    private UUID fareId;
    private String fareAirlineCode;
    private CabinClass fareCabinClass;
    private String fareName;
    private boolean isBaggageIncluded;
    private boolean isRefundable;

    private UUID seatId;
    private String seatNumber;
    private CabinClass seatCabinClass;
    private SeatType seatType;
    private boolean hasExtraLegroom;
    private boolean isExitRow;

    private BigDecimal totalPrice;

}
