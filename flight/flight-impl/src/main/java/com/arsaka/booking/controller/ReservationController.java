package com.arsaka.booking.controller;

import com.arsaka.booking.service.TicketService;
import com.arsaka.controller.ReservationApi;
import com.arsaka.event.FlightsHoldEventRequest;
import com.arsaka.booking.service.FlightEventOrchestratorService;
import com.arsaka.search.request.ReservationsSearchRequest;
import com.arsaka.search.response.ReservationSearchResponse;
import com.arsaka.search.response.ReservationsSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReservationController implements ReservationApi {

    private final TicketService ticketService;
    private final FlightEventOrchestratorService service;

    @Override
    public ResponseEntity<ReservationsSearchResponse> getTickets(UUID accountId, ReservationsSearchRequest request) {
        ReservationsSearchResponse response = ticketService.getTickets(accountId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ReservationSearchResponse> getTicket(UUID accountId, UUID ticketId) {
        ReservationSearchResponse response = ticketService.getTicket(accountId, ticketId);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<Void> hold(UUID accountId, FlightsHoldEventRequest request) {
        service.hold(request, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> cancel(UUID accountId, UUID bookingId) {
        service.cancel(accountId, bookingId);
        return ResponseEntity.noContent().build();
    }
}
