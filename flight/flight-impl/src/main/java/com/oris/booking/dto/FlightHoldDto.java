package com.oris.booking.dto;

import com.oris.common.CabinClass;
import com.oris.common.PassengerType;
import com.oris.event.dto.FlightPassenger;
import com.oris.pricing.dto.PricingAdjRecord;
import com.oris.pricing.dto.PricingRuleRecord;
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
