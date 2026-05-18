package com.arsaka.booking.util;

import com.arsaka.jooq.tables.Ticket;
import org.jooq.SortField;

import java.util.ArrayList;
import java.util.List;

public class TicketOrderBuilder {

    public static List<SortField<?>> build(
            Ticket ticket
    ) {
        List<SortField<?>> orderList = new ArrayList<>();

        orderList.add(ticket.CREATED_AT.asc());
        orderList.add(ticket.ID.asc());

        return orderList;
    }

}
