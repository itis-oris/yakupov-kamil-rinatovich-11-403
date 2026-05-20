package com.arsaka.flight.util;

import com.arsaka.common.CabinClass;
import com.arsaka.jooq.tables.FlightInventory;
import org.jooq.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlightInventoryConditionBuilder {

    public static List<Condition> build(
            UUID flightId,
            CabinClass cabinClass,
            FlightInventory fInv
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(fInv.FLIGHT_ID.eq(flightId));
        conditions.add(fInv.CABIN_CLASS.eq(cabinClass.name()));

        return conditions;
    }

}
