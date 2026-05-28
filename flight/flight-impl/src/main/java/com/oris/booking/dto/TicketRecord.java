package com.oris.booking.dto;

import com.oris.common.CabinClass;
import com.oris.common.PassengerType;
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
