package com.arsaka.flightsearch.repository;

import com.arsaka.common.CabinClass;
import com.arsaka.flightsearch.util.FlightInventoryConditionBuilder;
import com.arsaka.jooq.tables.FlightInventory;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.arsaka.jooq.Tables.FLIGHT_INVENTORY;

@Repository
@RequiredArgsConstructor
public class FlightInventoryQueryRepository {

    private final DSLContext dsl;

    private final static FlightInventory fInv = FLIGHT_INVENTORY.as("fInv");

    public BigDecimal getPrice(UUID flightId, CabinClass cabinClass) {
        List<Condition> conditions = FlightInventoryConditionBuilder.build(
                flightId,
                cabinClass,
                fInv
        );

        return dsl
                .select(
                        fInv.PRICE
                )
                .from(fInv)
                .where(conditions)
                .fetchOneInto(BigDecimal.class);
    }

    public boolean setHeld(UUID flightId, CabinClass cabinClass) {
        List<Condition> conditions = FlightInventoryConditionBuilder.build(
                flightId,
                cabinClass,
                fInv
        );

        int affectedRows = dsl
                .update(fInv)
                .set(fInv.AVAILABLE_SEATS, fInv.AVAILABLE_SEATS.minus(1))
                .set(fInv.HELD_SEATS, fInv.HELD_SEATS.plus(1))
                .set(fInv.UPDATED_AT, Instant.now())
                .where(conditions)
                .execute();

        return affectedRows == 1;
    }

    public void releaseHold(UUID flightId, CabinClass cabinClass) {
        List<Condition> conditions = FlightInventoryConditionBuilder.build(
                flightId,
                cabinClass,
                fInv
        );

        dsl
                .update(fInv)
                .set(fInv.AVAILABLE_SEATS, fInv.AVAILABLE_SEATS.plus(1))
                .set(fInv.HELD_SEATS, fInv.HELD_SEATS.minus(1))
                .set(fInv.UPDATED_AT, Instant.now())
                .where(conditions)
                .execute();
    }
}
