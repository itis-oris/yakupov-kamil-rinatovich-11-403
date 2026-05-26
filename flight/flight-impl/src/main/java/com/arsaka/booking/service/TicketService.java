package com.arsaka.booking.service;

import com.arsaka.booking.dto.TicketSearchRecord;
import com.arsaka.booking.dto.TicketWithDetailsSearchRecord;
import com.arsaka.booking.exception.TicketAccessDeniedException;
import com.arsaka.booking.exception.TicketNotFoundException;
import com.arsaka.booking.mapper.TicketMapper;
import com.arsaka.booking.util.TicketCursorUtil;
import com.arsaka.booking.util.TicketSearchCursor;
import com.arsaka.search.request.ReservationsSearchRequest;
import com.arsaka.search.request.dto.TicketStatus;
import com.arsaka.booking.repository.TicketRepository;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.event.dto.FlightPassenger;
import com.arsaka.event.dto.FlightPassengerDocument;
import com.arsaka.booking.dto.TicketRecord;
import com.arsaka.search.response.ReservationSearchResponse;
import com.arsaka.search.response.ReservationsSearchResponse;
import com.arsaka.search.response.dto.TicketSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository repository;
    private final TicketMapper mapper;

    public void save(UUID bookingId, Set<FlightHoldDto> flights) {
        for(FlightHoldDto flight: flights) {

            FlightPassenger passenger = flight.getPassenger();
            FlightPassengerDocument document = passenger.document();

            repository.save(
                    bookingId,
                    flight.getFlightId(),
                    passenger.firstName(),
                    passenger.lastName(),
                    passenger.dateOfBirth(),
                    passenger.gender(),
                    document.type(),
                    document.number(),
                    document.countryCode(),
                    flight.getPassengerType(),
                    flight.getFareId(),
                    flight.getSeatId(),
                    flight.getTotalPrice()
            );
        }
    }

    public List<TicketRecord> releaseHoldAndGetTickets(UUID bookingId) {
        return repository.updateAndGetTickets(bookingId, TicketStatus.CANCELLED);
    }

    public List<TicketRecord> confirmAndGetTickets(UUID bookingId) {
        return repository.updateAndGetTickets(bookingId, TicketStatus.CONFIRMED);
    }

    public ReservationsSearchResponse getTickets(UUID accountId, ReservationsSearchRequest request) {
        if(request == null) {
            request = new ReservationsSearchRequest(null, null);
        }
        TicketSearchCursor cursor = TicketCursorUtil.decode(request.nextCursor());

        List<TicketSearchRecord> ticketSearchRecords = repository.findTickets(
                accountId,
                request.status(),
                cursor,
                TicketCursorUtil.cursorLimit
        );

        List<TicketSearch> tickets = mapper.mapToList(ticketSearchRecords);

        return new ReservationsSearchResponse(tickets, TicketCursorUtil.getNext(ticketSearchRecords));
    }

    public ReservationSearchResponse getTicket(UUID accountId, UUID ticketId) {
        TicketWithDetailsSearchRecord record = repository.findTicket(accountId, ticketId);

        if(record == null) {
            log.debug("ticked not found exception | ticketId={}", ticketId);
            throw new TicketNotFoundException();
        }

        if(!record.getAccountId().equals(accountId)) {
            log.debug("Access denied to ticket exception | account id={} | ticket id={}", accountId, ticketId);
            throw new TicketAccessDeniedException();
        }

        return mapper.map(record);

    }

}
