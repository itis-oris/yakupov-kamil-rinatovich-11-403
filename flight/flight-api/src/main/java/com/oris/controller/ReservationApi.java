package com.oris.controller;

import com.oris.event.FlightsHoldEventRequest;
import com.oris.search.request.ReservationsSearchRequest;
import com.oris.search.response.ReservationSearchResponse;
import com.oris.search.response.ReservationsSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User reservations",
        description = "API for user reservations management: get all tickets, get info by one ticket, create(hold) ticket, cancel ticket")
@RestController
@RequestMapping("/api/v1/flights/reservations")
public interface ReservationApi {

    @Operation(summary = "Get user tickets")
    @PostMapping("/search")
    ResponseEntity<ReservationsSearchResponse> getTickets(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UUID accountId,
            @Valid @RequestBody(required = false) ReservationsSearchRequest request
    );

    @Operation(summary = "Get info by one ticket")
    @GetMapping("/{ticketId}")
    ResponseEntity<ReservationSearchResponse> getTicket(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UUID accountId,
            @PathVariable UUID ticketId
    );

    @Operation(summary = "Create tickets")
    @PostMapping("/hold")
    ResponseEntity<Void> hold(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UUID accountId,
            @Valid @RequestBody FlightsHoldEventRequest request
    );

    @Operation(summary = "Cancel all user tickets by booking")
    @DeleteMapping("/{bookingId}")
    ResponseEntity<Void> cancel(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UUID accountId,
            @PathVariable UUID bookingId
    );
}
