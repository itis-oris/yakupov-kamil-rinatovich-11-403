package com.oris.booking.service;

import com.oris.booking.dto.TicketRecord;
import com.oris.booking.mapper.TicketMapper;
import com.oris.event.FlightsHoldEventRequest;
import com.oris.flight.service.FlightInventoryService;
import com.oris.booking.dto.FlightHoldDto;
import com.oris.booking.mapper.FlightReserveEventMapper;
import com.oris.pricing.service.PricingService;
import com.oris.referencedata.service.SeatService;
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
        log.debug("starting flights hold | event={} | accountId={}", event, accountId);
        UUID bookingId = UUID.randomUUID();

        log.debug("starting flights mapping | event={} | accountId={} | bookingId={}", event, accountId, bookingId);
        Set<FlightHoldDto> flights = flightReserveEventMapper.map(event);
        log.debug("flights mapped | event={} | accountId={} | flights={} | bookingId={}", event, accountId, flights, bookingId);

        log.debug("starting flights price validation | event={} | accountId={} | bookingId={}", event, accountId, bookingId);
        pricingService.validatePrice(flights);
        log.debug("flights price validated | event={} | accountId={} | bookingId={}", event, accountId, bookingId);

        log.debug("starting flights booking saving | event={} | accountId={} | bookingId={}", event, accountId, bookingId);
        bookingService.save(bookingId, accountId, flights);
        log.debug("flights booking saved | event={} | accountId={} | bookingId={}", event, accountId, bookingId);

        log.debug("starting flights seat hold | event={} | accountId={} | bookingId={}", event, accountId, bookingId);
        seatService.holdSeats(bookingId, flights);
        log.debug("flights seat held | event={} | accountId={} | bookingId={}", event, accountId, bookingId);

        log.debug("starting flights inventory hold | event={} | accountId={} | bookingId={}", event, accountId, bookingId);
        flightInventoryService.holdSeats(flights);
        log.debug("flights inventory held | event={} | accountId={} | bookingId={}", event, accountId, bookingId);

        log.debug("starting flights tickets saving | event={} | accountId={} | bookingId={}", event, accountId, bookingId);
        ticketService.save(bookingId, flights);
        log.debug("flights tickets saved | event={} | accountId={} | bookingId={}", event, accountId, bookingId);

        log.debug("flights held | event={} | accountId={}", event, accountId);
    }

    @Transactional
    public void cancel(UUID accountId, UUID bookingId) {
        log.debug("starting flights hold cancel | bookingId={}", bookingId);

        List<TicketRecord> tickets = ticketService.releaseHoldAndGetTickets(bookingId);
        log.debug("get flights tickets to cancel | tickets={} | bookingId={}", tickets, bookingId);

        log.debug("starting flights booking ownership checking | bookingId={} | accountId={}", bookingId, accountId);
        bookingService.checkBookingAccountId(accountId, bookingId);
        log.debug("flights booking ownership check passed | bookingId={} | accountId={}", bookingId, accountId);

        log.debug("starting flights tickets mapping | bookingId={}", bookingId);
        ticketMapper.map(tickets);
        log.debug("flights tickets mapped | bookingId={}", bookingId);

        log.debug("starting flights seats hold releasing | bookingId={}", bookingId);
        seatService.releaseHold(bookingId);
        log.debug("flights seats hold released | bookingId={}", bookingId);

        log.debug("starting flights inventory hold releasing | bookingId={}", bookingId);
        flightInventoryService.releaseHold(tickets);
        log.debug("flights inventory hold released | bookingId={}", bookingId);

        log.debug("starting flights booking hold releasing | bookingId={}", bookingId);
        bookingService.releaseHold(bookingId);
        log.debug("flights inventory booking released | bookingId={}", bookingId);
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
