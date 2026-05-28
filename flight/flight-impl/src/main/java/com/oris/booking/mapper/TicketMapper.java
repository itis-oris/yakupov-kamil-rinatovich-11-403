package com.oris.booking.mapper;

import com.oris.booking.dto.TicketSearchRecord;
import com.oris.booking.dto.TicketWithDetailsSearchRecord;
import com.oris.common.CabinClass;
import com.oris.booking.dto.TicketRecord;
import com.oris.event.dto.FlightPassenger;
import com.oris.event.dto.FlightPassengerDocument;
import com.oris.pricing.service.FareService;
import com.oris.search.response.ReservationSearchResponse;
import com.oris.search.response.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final FareService fareService;

    public void map(List<TicketRecord> tickets) {
        for(TicketRecord ticket: tickets) {
            CabinClass cabinClass = fareService.getCabinClass(ticket.getFareId());
            ticket.setCabinClass(cabinClass);
        }
    }

    public List<TicketSearch> mapToList(List<TicketSearchRecord> tickets) {
        List<TicketSearch> result = new ArrayList<>();
        for(TicketSearchRecord ticket: tickets) {
            FlightPassengerDocument flightPassengerDocument = new FlightPassengerDocument(
                    ticket.getDocumentType(),
                    ticket.getDocumentNumber(),
                    ticket.getDocumentCountryCode()
            );

            FlightPassenger flightPassenger = new FlightPassenger(
                    ticket.getPassengerFirstName(),
                    ticket.getPassengerLastName(),
                    ticket.getPassengerDateOfBirth(),
                    ticket.getPassengerGender(),
                    flightPassengerDocument
            );

            TicketSearch ticketSearch = new TicketSearch(
                    ticket.getId(),
                    ticket.getStatus(),
                    ticket.getBookingId(),
                    flightPassenger,
                    ticket.getPassengerType(),
                    ticket.getTotalPrice(),
                    ticket.getCreatedAt()
            );

            result.add(ticketSearch);
        }

        return result;
    }

    public ReservationSearchResponse map(TicketWithDetailsSearchRecord record) {
        FlightPassengerDocument flightPassengerDocument = new FlightPassengerDocument(
                record.getDocumentType(),
                record.getDocumentNumber(),
                record.getDocumentCountryCode()
        );

        FlightPassenger flightPassenger = new FlightPassenger(
                record.getPassengerFirstName(),
                record.getPassengerLastName(),
                record.getPassengerDateOfBirth(),
                record.getPassengerGender(),
                flightPassengerDocument
        );

        TicketSearch ticketSearch = new TicketSearch(
                record.getId(),
                record.getStatus(),
                record.getBookingId(),
                flightPassenger,
                record.getPassengerType(),
                record.getTotalPrice(),
                record.getCreatedAt()
        );

        FlightsSearchAirport departureAirport = new FlightsSearchAirport(
                record.getDepartureAirportCode(),
                record.getDepartureAirportName(),
                record.getDepartureAirportCityName(),
                record.getDepartureAirportCountryName()
        );

        FlightsSearchAirport arrivalAirport = new FlightsSearchAirport(
                record.getArrivalAirportCode(),
                record.getArrivalAirportName(),
                record.getArrivalAirportCityName(),
                record.getArrivalAirportCountryName()
        );

        FlightSearch flightSearch =  new FlightSearch(
                record.getFlightId(),
                departureAirport,
                arrivalAirport,
                record.getScheduledDeparture(),
                record.getScheduledArrival(),
                null
        );

        FlightFare flightFare = new FlightFare(
                record.getFareId(),
                record.getFareAirlineCode(),
                record.getFareCabinClass(),
                record.getFareName(),
                record.isBaggageIncluded(),
                record.isRefundable()
        );

        FlightSeat flightSeat = null;

        if(record.getSeatId() != null) {
            flightSeat = new FlightSeat(
                    record.getSeatId(),
                    record.getSeatNumber(),
                    record.getSeatCabinClass(),
                    record.getSeatType(),
                    record.isHasExtraLegroom(),
                    record.isExitRow(),
                    null
            );
        }

        return new ReservationSearchResponse(
                ticketSearch,
                flightSearch,
                flightFare,
                flightSeat,
                record.getTotalPrice()
        );
    }

}
