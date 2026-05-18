package com.arsaka.booking.dto;

import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
import com.arsaka.event.dto.FlightPassenger;
import com.arsaka.pricing.dto.PricingAdjRecord;
import com.arsaka.pricing.dto.PricingRuleRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FlightHoldDto {
    private UUID flightId;
    private UUID seatId;
    private UUID fareId;
    private FlightPassenger passenger;
    private PassengerType passengerType;
    private String priceHash;

    private CabinClass seatCabinClass;
    private CabinClass fareCabinClass;
    private BigDecimal basePrice;
    private PricingAdjRecord adjRecord;
    private PricingRuleRecord ruleRecord;
    private BigDecimal totalPrice;
}
