package com.arsaka.pricing.service;

import com.arsaka.common.PassengerType;
import com.arsaka.flightsearch.dto.FlightRecord;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.pricing.dto.FlightPricingRecord;
import com.arsaka.pricing.dto.PricingAdjRecord;
import com.arsaka.pricing.dto.PricingRecord;
import com.arsaka.pricing.dto.PricingRuleRecord;
import com.arsaka.pricing.exception.PricingRuleNotFoundException;
import com.arsaka.search.request.FlightWithFaresRequest;
import com.arsaka.search.response.dto.FlightFare;
import com.arsaka.search.response.dto.FlightPassengerTypePrice;
import com.arsaka.search.response.dto.FlightPassengersTypePrice;
import com.arsaka.search.response.dto.FlightPassengersTypePriceMap;
import com.arsaka.pricing.mapper.FlightReserveDtoPricingMapper;
import com.arsaka.pricing.exception.PriceNotFoundException;
import com.arsaka.pricing.exception.PriceNotValidException;
import com.arsaka.pricing.repository.PricingRepository;
import com.arsaka.pricing.util.PricingCalculator;
import com.arsaka.pricing.util.PricingHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingService {

    private final PricingRepository repository;
    private final FlightReserveDtoPricingMapper flightReserveDtoPricingMapper;

    public Map<UUID, FlightPassengersTypePriceMap> getPrices(
            FlightRecord flightRecord,
            Set<FlightFare> fares,
            FlightWithFaresRequest request
    ) {
        Map<UUID, FlightPassengersTypePriceMap> result = new HashMap<>();

        for (FlightFare fare : fares) {
            FlightPricingRecord record = repository.getPrices(
                    flightRecord.getFlightId(),
                    fare.fareId(),
                    request.passengers().keySet()
            );

            Set<PricingRuleRecord> pricingRuleRecords = record.getRuleRecord();

            if(pricingRuleRecords.isEmpty()) {
                log.debug("Pricing rule not found exception | fare id={} | passengers={}", fare.fareId(), request.passengers().keySet());
                throw new PricingRuleNotFoundException();
            }

            PricingAdjRecord pricingAdjRecord = record.getAdjRecord();


            Map<PassengerType, FlightPassengersTypePrice> prices = new HashMap<>();
            for (PricingRuleRecord pricingRuleRecord : pricingRuleRecords) {
                prices.put(
                        pricingRuleRecord.getPassengerType(),
                        new FlightPassengersTypePrice(
                                request.passengers().entrySet().stream()
                                        .filter(e -> e.getKey() == pricingRuleRecord.getPassengerType())
                                        .mapToInt(Map.Entry::getValue)
                                        .sum(),

                                PricingCalculator.calculate(
                                        flightRecord.getPrice(),
                                        pricingAdjRecord,
                                        pricingRuleRecord
                                ),

                                PricingHasher.hash(
                                        flightRecord.getFlightId(),
                                        fare.fareId(),
                                        flightRecord.getPrice(),
                                        pricingRuleRecord,
                                        pricingAdjRecord
                                )
                        )
                );
            }

            result.put(fare.fareId(), new FlightPassengersTypePriceMap(prices));
        }

        return result;
    }

    public FlightPassengerTypePrice getPrice(
            UUID flightId,
            UUID fareId,
            PassengerType passengerType,
            BigDecimal basePrice
    ) {
        PricingRecord record = repository.getPrice(
                flightId,
                fareId,
                passengerType
        );

        if (record == null) {
            log.debug("Price not found exception | flightId={} | fareId={} | passengerType={}", flightId, fareId, passengerType);
            throw new PriceNotFoundException();
        }


        return new FlightPassengerTypePrice(
                passengerType,

                PricingCalculator.calculate(
                        basePrice,
                        record.getAdjRecord(),
                        record.getRuleRecord()
                ),

                PricingHasher.hash(
                        flightId,
                        fareId,
                        basePrice,
                        record.getRuleRecord(),
                        record.getAdjRecord()
                )
        );

    }

    public void validatePrice(Set<FlightHoldDto> flights) {
        for (FlightHoldDto flight : flights) {

            PricingRecord record = repository.getPrice(
                    flight.getFlightId(),
                    flight.getFareId(),
                    flight.getPassengerType()
            );

            flightReserveDtoPricingMapper.map(flight, record);

            String hash = PricingHasher.hash(
                    flight.getFlightId(),
                    flight.getFareId(),
                    flight.getBasePrice(),
                    flight.getRuleRecord(),
                    flight.getAdjRecord()
            );

            BigDecimal price = PricingCalculator.calculate(
                    flight.getBasePrice(),
                    record.getAdjRecord(),
                    record.getRuleRecord()
            );

            if (!flight.getPriceHash().equals(hash) || price.compareTo(flight.getTotalPrice()) != 0) {
                log.debug("Price not valid exception | flightId={} | seatId={} | fareId={} | passengerType = {}",
                        flight.getFlightId(),
                        flight.getFareId(),
                        flight.getSeatId(),
                        flight.getPassengerType()
                );
                throw new PriceNotValidException();
            }
        }
    }


}
