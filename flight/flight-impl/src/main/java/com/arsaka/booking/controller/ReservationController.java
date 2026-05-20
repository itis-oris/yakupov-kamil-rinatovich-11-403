package com.arsaka.booking.controller;

import com.arsaka.booking.service.TicketService;
import com.arsaka.event.FlightsHoldEventRequest;
import com.arsaka.booking.service.FlightEventOrchestratorService;
import com.arsaka.search.request.ReservationsSearchRequest;
import com.arsaka.search.response.ReservationSearchResponse;
import com.arsaka.search.response.ReservationsSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flights/reservations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReservationController {

    private final TicketService ticketService;
    private final FlightEventOrchestratorService service;

    @PostMapping("/search")
    public ResponseEntity<ReservationsSearchResponse> getTickets(
            @AuthenticationPrincipal UUID accountId,
            @Valid @RequestBody(required = false) ReservationsSearchRequest request
    ) {
        if(request == null) {
            request = new ReservationsSearchRequest(null, null);
        }
        ReservationsSearchResponse response = ticketService.getTickets(accountId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<ReservationSearchResponse> getTicket(
            @AuthenticationPrincipal UUID accountId,
            @PathVariable UUID ticketId
    ) {
        ReservationSearchResponse response = ticketService.getTicket(accountId, ticketId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/hold")
    public ResponseEntity<Void> hold(
            @AuthenticationPrincipal UUID accountId,
            @Valid @RequestBody FlightsHoldEventRequest request
    ) {
        service.hold(request, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancel(@PathVariable UUID bookingId) {
        service.cancel(bookingId);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/{bookingId}")
//    public ResponseEntity<Void> reserve(@PathVariable UUID bookingId) {
//        service.reserve(bookingId);
//        return ResponseEntity.noContent().build();
//    }
}
