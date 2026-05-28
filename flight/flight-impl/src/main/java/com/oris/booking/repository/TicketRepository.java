package com.oris.booking.repository;

import com.oris.booking.dto.TicketSearchRecord;
import com.oris.booking.dto.TicketWithDetailsSearchRecord;
import com.oris.booking.util.TicketConditionBuilder;
import com.oris.booking.util.TicketOrderBuilder;
import com.oris.booking.util.TicketSearchCursor;
import com.oris.jooq.tables.*;
import com.oris.search.request.dto.TicketStatus;
import com.oris.event.dto.DocumentType;
import com.oris.event.dto.PassengerGender;
import com.oris.common.PassengerType;
import com.oris.booking.dto.TicketRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.oris.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private final DSLContext dsl;

    private final static Ticket ticket = TICKET.as("ticket");
    private final static Booking booking = BOOKING.as("booking");
    private final static Flight flight = FLIGHT.as("flight");
    private final static Route route = ROUTE.as("route");
    private final static Airport depAir = AIRPORT.as("depAir");
    private final static Country depCountry = COUNTRY.as("depCountry");
    private final static City depCity = CITY.as("depCity");
    private final static Airport arrAir = AIRPORT.as("arrAir");
    private final static Country arrCountry = COUNTRY.as("arrCountry");
    private final static City arrCity = CITY.as("arrCity");
    private final static Fare fare = FARE.as("fare");
    private final static Seat seat = SEAT.as("seat");


    public void save(
            UUID bookingId,
            UUID flightId,
            String passengerFirstName,
            String passengerLastName,
            LocalDate passengerDateOfBirth,
            PassengerGender passengerGender,
            DocumentType documentType,
            String documentNumber,
            String documentCountryCode,
            PassengerType passengerType,
            UUID fareId,
            UUID seatId,
            BigDecimal totalPrice
    ) {
        dsl
                .insertInto(ticket)
                .columns(
                        ticket.STATUS,
                        ticket.BOOKING_ID,
                        ticket.FLIGHT_ID,
                        ticket.PASSENGER_FIRST_NAME,
                        ticket.PASSENGER_LAST_NAME,
                        ticket.PASSENGER_DATE_OF_BIRTH,
                        ticket.PASSENGER_GENDER,
                        ticket.DOCUMENT_TYPE,
                        ticket.DOCUMENT_NUMBER,
                        ticket.DOCUMENT_COUNTRY_CODE,
                        ticket.PASSENGER_TYPE,
                        ticket.FARE_ID,
                        ticket.SEAT_ID,
                        ticket.TOTAL_PRICE
                )
                .values(
                        TicketStatus.ISSUED.name(),
                        bookingId,
                        flightId,
                        passengerFirstName,
                        passengerLastName,
                        passengerDateOfBirth,
                        passengerGender.name(),
                        documentType.name(),
                        documentNumber,
                        documentCountryCode,
                        passengerType.name(),
                        fareId,
                        seatId,
                        totalPrice
                )
                .execute();
    }

    public List<TicketRecord> updateAndGetTickets(UUID bookingId, TicketStatus status) {
        return dsl
                .update(ticket)
                .set(ticket.STATUS, status.name())
                .set(ticket.UPDATED_AT, Instant.now())
                .where(ticket.BOOKING_ID.eq(bookingId))
                .returning(
                        ticket.ID,
                        ticket.BOOKING_ID,
                        ticket.FLIGHT_ID,
                        ticket.PASSENGER_TYPE,
                        ticket.FARE_ID,
                        ticket.SEAT_ID
                )
                .fetchInto(TicketRecord.class);
    }

    public List<TicketSearchRecord> findTickets(
            UUID accountId,
            TicketStatus status,
            TicketSearchCursor cursor,
            int cursorLimit
    ) {
        List<Condition> conditions = TicketConditionBuilder.build(
                accountId,
                status,
                cursor,
                ticket,
                booking
        );

        List<SortField<?>> orderBy = TicketOrderBuilder.build(ticket);

        return dsl.
                        select(
                                ticket.ID,
                                ticket.STATUS,
                                ticket.BOOKING_ID,
                                ticket.PASSENGER_FIRST_NAME,
                                ticket.PASSENGER_LAST_NAME,
                                ticket.PASSENGER_DATE_OF_BIRTH,
                                ticket.PASSENGER_GENDER,
                                ticket.DOCUMENT_TYPE,
                                ticket.DOCUMENT_NUMBER,
                                ticket.DOCUMENT_COUNTRY_CODE,
                                ticket.PASSENGER_TYPE,
                                ticket.TOTAL_PRICE,
                                ticket.CREATED_AT
                        )
                        .from(ticket)
                        .join(booking).on(booking.ID.eq(ticket.BOOKING_ID))
                        .where(conditions)
                        .orderBy(orderBy)
                        .limit(cursorLimit)
                        .fetchInto(TicketSearchRecord.class);
    }

    public TicketWithDetailsSearchRecord findTicket(
            UUID accountId,
            UUID ticketId
    ) {

        return dsl.
                select(
                        ticket.ID,
                        ticket.STATUS,
                        ticket.BOOKING_ID,
                        ticket.PASSENGER_FIRST_NAME,
                        ticket.PASSENGER_LAST_NAME,
                        ticket.PASSENGER_DATE_OF_BIRTH,
                        ticket.PASSENGER_GENDER,
                        ticket.DOCUMENT_TYPE,
                        ticket.DOCUMENT_NUMBER,
                        ticket.DOCUMENT_COUNTRY_CODE,
                        ticket.PASSENGER_TYPE,
                        ticket.TOTAL_PRICE,
                        ticket.CREATED_AT,
                        booking.ACCOUNT_ID,
                        flight.ID.as("flightId"),
                        depAir.CODE.as("departureAirportCode"),
                        depAir.NAME.as("departureAirportName"),
                        depCity.NAME.as("departureAirportCityName"),
                        depCountry.NAME.as("departureAirportCountryName"),
                        arrAir.CODE.as("arrivalAirportCode"),
                        arrAir.NAME.as("arrivalAirportName"),
                        arrCity.NAME.as("arrivalAirportCityName"),
                        arrCountry.NAME.as("arrivalAirportCountryName"),
                        flight.SCHEDULED_DEPARTURE.as("scheduledDeparture"),
                        flight.SCHEDULED_ARRIVAL.as("scheduledArrival"),
                        fare.ID.as("fareId"),
                        fare.AIRLINE_CODE.as("fareAirlineCode"),
                        fare.CABIN_CLASS.as("fareCabinClass"),
                        fare.NAME.as("fareName"),
                        fare.BAGGAGE_INCLUDED.as("isBaggageIncluded"),
                        fare.IS_REFUNDABLE.as("isRefundable"),
                        seat.ID.as("seatId"),
                        seat.NUMBER.as("seatNumber"),
                        seat.CABIN_CLASS.as("seatCabinClass"),
                        seat.TYPE.as("seatType"),
                        seat.HAS_EXTRA_LEGROOM.as("hasExtraLegroom"),
                        seat.IS_EXIT_ROW.as("isExitRow")
                )
                .from(ticket)
                .join(booking).on(ticket.BOOKING_ID.eq(booking.ID))
                .join(flight).on(ticket.FLIGHT_ID.eq(flight.ID))
                .join(route).on(flight.ROUTE_ID.eq(route.ID))
                .join(depAir).on(route.DEPARTURE_AIRPORT_CODE.eq(depAir.CODE))
                .join(arrAir).on(route.ARRIVAL_AIRPORT_CODE.eq(arrAir.CODE))
                .join(depCity).on(depAir.CITY_CODE.eq(depCity.CODE))
                .join(depCountry).on(depCity.COUNTRY_CODE.eq(depCountry.CODE))
                .join(arrCity).on(arrAir.CITY_CODE.eq(arrCity.CODE))
                .join(arrCountry).on(arrCity.COUNTRY_CODE.eq(arrCountry.CODE))
                .join(fare).on(ticket.FARE_ID.eq(fare.ID))
                .leftJoin(seat).on(ticket.SEAT_ID.eq(seat.ID))
                .where(booking.ACCOUNT_ID.eq(accountId)
                        .and(ticket.ID.eq(ticketId))
                )
                .fetchOneInto(TicketWithDetailsSearchRecord.class);
    }
}
