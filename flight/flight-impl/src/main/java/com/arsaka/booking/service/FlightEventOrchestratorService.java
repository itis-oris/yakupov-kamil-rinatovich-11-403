package com.arsaka.booking.service;

import com.arsaka.booking.dto.TicketRecord;
import com.arsaka.booking.mapper.TicketMapper;
import com.arsaka.event.FlightsHoldEventRequest;
import com.arsaka.flightsearch.service.FlightInventoryService;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.booking.mapper.FlightReserveEventMapper;
import com.arsaka.pricing.service.PricingService;
import com.arsaka.referencedata.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FlightEventOrchestratorService {

    private final SeatService seatService;
    private final PricingService pricingService;
    private final FlightInventoryService flightInventoryService;
    private final FlightReserveEventMapper flightReserveEventMapper;
    private final BookingService bookingService;
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Transactional
    public void hold(FlightsHoldEventRequest event, UUID accountId) {
        UUID bookingId = UUID.randomUUID();

        Set<FlightHoldDto> flights = flightReserveEventMapper.map(event);

        seatService.holdSeats(bookingId, flights);

        flightInventoryService.holdSeats(flights);

        pricingService.validatePrice(flights);

        bookingService.save(bookingId, accountId, flights);

        ticketService.save(bookingId, flights);
    }

    @Transactional
    public void cancel(UUID bookingId) {
        List<TicketRecord> tickets = ticketService.releaseHoldAndGetTickets(bookingId);

        ticketMapper.map(tickets);

        seatService.releaseHold(bookingId);

        flightInventoryService.releaseHold(tickets);

        bookingService.releaseHold(bookingId);
    }

//    @Transactional
//    public void reserve(UUID bookingId) {
//        List<TicketRecord> tickets = ticketService.confirmAndGetTickets(bookingId);
//
//        ticketMapper.map(tickets);
//
//        seatService.reserve(bookingId);
//
//        bookingService.confirm(bookingId);
//    }
}
