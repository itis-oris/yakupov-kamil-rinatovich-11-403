package com.oris.flight.util;

import com.oris.search.request.dto.OrderType;
import com.oris.jooq.tables.Flight;
import com.oris.jooq.tables.FlightInventory;
import org.jooq.SortField;

import java.util.ArrayList;
import java.util.List;

public class FlightOrderBuilder {

    public static List<SortField<?>> build(
            OrderType orderType,
            Flight flight,
            FlightInventory fInv
    ) {
        List<SortField<?>> orderList = new ArrayList<>();

        switch (orderType) {
            case DEPARTURE_ASC -> {
                orderList.add(flight.SCHEDULED_DEPARTURE.asc());
                orderList.add(flight.ID.asc());
            }

            case PRICE_ASC -> {
                orderList.add(fInv.PRICE.asc());
                orderList.add(fInv.FLIGHT_ID.asc());

            }
            case PRICE_DESC -> {
                orderList.add(fInv.PRICE.desc());
                orderList.add(fInv.FLIGHT_ID.desc());
            }
        }

        return orderList;
    }

}
