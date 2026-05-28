package com.oris.referencedata.repository;

import com.oris.common.CabinClass;
import com.oris.search.response.dto.FlightSeat;
import com.oris.search.response.dto.SeatReservedStatus;
import com.oris.jooq.tables.Seat;
import com.oris.jooq.tables.SeatReserved;
import com.oris.referencedata.util.SeatConditionBuilder;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.oris.jooq.Tables.SEAT;
import static com.oris.jooq.Tables.SEAT_RESERVED;

@Repository
@RequiredArgsConstructor
public class SeatRepository {

    private final DSLContext dsl;

    private final static Seat seat = SEAT.as("seat");
    private final static SeatReserved seatRes = SEAT_RESERVED.as("seatRes");

    public Set<FlightSeat> getSeats(UUID flightId) {
        return new HashSet<>(dsl
                .select(
                        seat.ID.as("seatId"),
                        seat.NUMBER.as("number"),
                        seat.CABIN_CLASS.as("cabinClass"),
                        seat.TYPE.as("type"),
                        seat.HAS_EXTRA_LEGROOM.as("hasExtraLegroom"),
                        seat.IS_EXIT_ROW.as("isExitRow"),
                        seatRes.STATUS.as("status")
                )
                .from(seatRes)
                .join(seat).on(seat.ID.eq(seatRes.SEAT_ID))
                .where(seatRes.FLIGHT_ID.eq(flightId))
                .fetchInto(FlightSeat.class)
        );
    }

    public FlightSeat getSeat(UUID flightId, UUID seatId) {
        List<Condition> conditions = SeatConditionBuilder.build(flightId, seatId, seatRes);

        return dsl
                .select(
                        seat.ID.as("seatId"),
                        seat.NUMBER.as("number"),
                        seat.CABIN_CLASS.as("cabinClass"),
                        seat.TYPE.as("type"),
                        seat.HAS_EXTRA_LEGROOM.as("hasExtraLegroom"),
                        seat.IS_EXIT_ROW.as("isExitRow"),
                        seatRes.STATUS.as("status")
                )
                .from(seatRes)
                .join(seat).on(seat.ID.eq(seatRes.SEAT_ID))
                .where(conditions)
                .fetchOneInto(FlightSeat.class);
    }

    public boolean hold(
            UUID flightId,
            UUID seatId,
            UUID bookingId
    ) {
        List<Condition> conditions = SeatConditionBuilder.buildAvailableSeats(flightId, seatId, seatRes);
        int affectedRows = dsl
                .update(seatRes)
                .set(seatRes.STATUS, SeatReservedStatus.HELD.name())
                .set(seatRes.HELD_BY_BOOKING_ID, bookingId)
                .where(conditions)
                .execute();

        return affectedRows == 1;
    }

    public CabinClass getCabinClass(UUID seatId) {

        return dsl
                .select(
                        seat.CABIN_CLASS
                )
                .from(seat)
                .where(seat.ID.eq(seatId))
                .fetchOneInto(CabinClass.class);
    }

    public void releaseHold(UUID bookingId) {
        List<Condition> conditions = SeatConditionBuilder.build(bookingId, seatRes);

        dsl
                .update(seatRes)
                .set(seatRes.STATUS, SeatReservedStatus.AVAILABLE.name())
                .setNull(seatRes.HELD_BY_BOOKING_ID)
                .set(seatRes.UPDATED_AT, Instant.now())
                .where(conditions)
                .execute();
    }

    public void reserve(UUID bookingId) {
        List<Condition> conditions = SeatConditionBuilder.build(bookingId, seatRes);

        dsl
                .update(seatRes)
                .set(seatRes.STATUS, SeatReservedStatus.SOLD.name())
                .set(seatRes.UPDATED_AT, Instant.now())
                .where(conditions)
                .execute();
    }
}
