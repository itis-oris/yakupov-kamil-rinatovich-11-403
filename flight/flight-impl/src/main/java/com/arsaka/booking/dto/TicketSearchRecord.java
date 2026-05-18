package com.arsaka.booking.dto;

import com.arsaka.common.PassengerType;
import com.arsaka.event.dto.DocumentType;
import com.arsaka.event.dto.PassengerGender;
import com.arsaka.search.request.dto.TicketStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TicketSearchRecord {
    private UUID id;
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

    private BigDecimal totalPrice;

    private Instant createdAt;
}
