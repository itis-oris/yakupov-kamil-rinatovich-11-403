package com.arsaka.pricing.mapper;

import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.pricing.dto.PricingRecord;
import com.arsaka.flightsearch.service.FlightInventoryService;
import com.arsaka.pricing.util.PricingCalculator;
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
