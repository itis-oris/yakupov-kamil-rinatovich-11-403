package com.arsaka.booking.dto;

import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
import lombok.Data;

import java.util.UUID;

@Data
public class TicketRecord {
    private UUID id;
    private UUID bookingId;
    private UUID flightId;
    private PassengerType passengerType;
    private UUID fareId;
    private UUID seatId;
    private CabinClass cabinClass;
}
