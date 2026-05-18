package com.arsaka.referencedata.util;

import com.arsaka.search.response.dto.SeatReservedStatus;
import com.arsaka.jooq.tables.SeatReserved;
import org.jooq.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SeatConditionBuilder {

    public static List<Condition> buildAvailableSeats(
            UUID flightId,
            UUID seatId,
            SeatReserved seatRes
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(seatRes.FLIGHT_ID.eq(flightId));
        conditions.add(seatRes.SEAT_ID.eq(seatId));
        conditions.add(seatRes.STATUS.eq(SeatReservedStatus.AVAILABLE.name()));

        return conditions;
    }

    public static List<Condition> build(
            UUID flightId,
            UUID seatId,
            SeatReserved seatRes
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(seatRes.FLIGHT_ID.eq(flightId));
        conditions.add(seatRes.SEAT_ID.eq(seatId));

        return conditions;
    }

    public static List<Condition> build(
            UUID bookingId,
            SeatReserved seatRes
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(seatRes.HELD_BY_BOOKING_ID.eq(bookingId));
        conditions.add(seatRes.STATUS.eq(SeatReservedStatus.HELD.name()));

        return conditions;
    }

}
