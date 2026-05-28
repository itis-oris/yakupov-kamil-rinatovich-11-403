package com.oris.pricing.mapper;

import com.oris.booking.dto.FlightHoldDto;
import com.oris.pricing.dto.PricingRecord;
import com.oris.flight.service.FlightInventoryService;
import com.oris.pricing.util.PricingCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class FlightReserveDtoPricingMapper {

    private final FlightInventoryService flightInventoryService;

    public void map(FlightHoldDto flight, PricingRecord record) {
        BigDecimal basePrice = flightInventoryService.getPrice(flight.getFlightId(), flight.getFareCabinClass());

        flight.setAdjRecord(record.getAdjRecord());
        flight.setRuleRecord(record.getRuleRecord());
        flight.setBasePrice(basePrice);
        flight.setTotalPrice(PricingCalculator.calculate(basePrice, record.getAdjRecord(), record.getRuleRecord()));
    }
}
