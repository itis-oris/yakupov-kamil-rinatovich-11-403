package com.oris.booking.repository;

import com.oris.booking.model.BookingStatus;
import com.oris.jooq.tables.Booking;
import com.oris.jooq.tables.records.BookingRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static com.oris.jooq.Tables.BOOKING;

@Repository
@RequiredArgsConstructor
public class BookingRepository {

    private final DSLContext dsl;

    private final Booking booking = BOOKING.as("booking");

    public boolean save(
            UUID bookingId,
            BigDecimal totalPrice,
            UUID accountId
    ) {
        int insertedRows = dsl
                .insertInto(booking)
                .columns(
                        booking.ID,
                        booking.STATUS,
                        booking.TOTAL_PRICE,
                        booking.ACCOUNT_ID
                )
                .values(
                        bookingId,
                        BookingStatus.HELD.name(),
                        totalPrice,
                        accountId
                )
                .onConflict(BOOKING.ID)
                .doNothing()
                .execute();

        return insertedRows == 1;
    }

    public BookingRecord findById(UUID bookingId) {
        return dsl
                .selectFrom(booking)
                .where(booking.ID.eq(bookingId))
                .fetchOne();
    }

    public boolean setStatus(UUID bookingId, BookingStatus status) {
         int updatedRows = dsl
                .update(booking)
                .set(booking.STATUS, status.name())
                .set(booking.UPDATED_AT, Instant.now())
                .where(booking.ID.eq(bookingId))
                .execute();

         return updatedRows != 0;
    }
}
