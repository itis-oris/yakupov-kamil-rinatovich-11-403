package com.arsaka.flightsearch.service;

import com.arsaka.flightsearch.dto.FlightRecord;
import com.arsaka.flightsearch.dto.FlightSearchDto;
import com.arsaka.flightsearch.dto.FlightSearchRecord;
import com.arsaka.flightsearch.mapper.FlightMapper;
import com.arsaka.flightsearch.mapper.FlightSearchMapper;
import com.arsaka.flightsearch.mapper.FlightSearchRequestMapper;
import com.arsaka.flightsearch.util.FlightCursorUtil;
import com.arsaka.pricing.service.FareService;
import com.arsaka.pricing.service.PricingService;
import com.arsaka.referencedata.service.SeatService;
import com.arsaka.search.request.FlightRequest;
import com.arsaka.search.request.FlightWithFaresRequest;
import com.arsaka.search.request.FlightsSearchRequest;
import com.arsaka.search.response.FlightSearchResponse;
import com.arsaka.search.response.FlightWithFaresResponse;
import com.arsaka.search.response.FlightsSearchResponse;
import com.arsaka.search.response.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FlightSearchOrchestratorService {

    private final FlightService flightService;
    private final FlightSearchMapper flightSearchMapper;
    private final FlightSearchFilterService flightSearchFilterService;
    private final SeatService seatService;
    private final FareService fareService;
    private final PricingService pricingService;
    private final FlightMapper flightMapper;

    @Transactional
    public FlightsSearchResponse getFlights(FlightsSearchRequest request) {
        FlightSearchDto dto = FlightSearchRequestMapper.map(request);

        List<FlightSearchRecord> flights = flightService.get(request, dto);
        log.info("get flight search record list | flights={}", flights);

        FlightsSearchFilter filter = flightSearchFilterService.getFilterResponse(dto);

        return new FlightsSearchResponse(
                flightSearchMapper.toResponseList(flights),
                filter,
                FlightCursorUtil.getNext(flights, dto.getOrderType()));
    }

    @Transactional
    public FlightWithFaresResponse getFlightWithFares(UUID flightId, FlightWithFaresRequest request) {
        FlightRecord flightRecord = flightService.get(flightId, request.cabinClass());
        log.info("get FlightRecord with fares from flight service | record={}", flightRecord);

        Set<FlightSeat> seats = seatService.getSeats(flightId);
        log.info("get seats from seat service | seats={}", seats);

        Set<FlightFare> fares = fareService.getFares(flightRecord.getAirlineCode(), flightRecord.getCabinClass());
        log.info("get fares from fare service | fares={}", fares);

        Map<UUID, FlightPassengersTypePriceMap> farePrices = pricingService.getPrices(flightRecord, fares, request);

        return flightMapper.map(flightRecord, seats, farePrices, fares);
    }

    @Transactional
    public FlightSearchResponse getFlight(UUID flightId, FlightRequest request) {
        FlightFare fare = fareService.getFare(request.fareId());
        log.info("get fare from fare service | fares={}", fare);

        FlightSeat seat = seatService.getSeat(flightId, request.seatId());
        log.info("get seat from seat service | seat={}", seat);

        FlightRecord flightRecord = flightService.get(flightId, fare.cabinClass());
        log.info("get FlightRecord from flight service | record={}", flightRecord);

        FlightPassengerTypePrice farePrice = pricingService.getPrice(
                flightId,
                request.fareId(),
                request.passengerType(),
                flightRecord.getPrice()
        );

        return flightMapper.map(flightRecord, seat, fare, farePrice);
    }
}
