package com.oris.booking.util;

import com.oris.search.request.dto.TicketStatus;
import com.oris.jooq.tables.Booking;
import com.oris.jooq.tables.Ticket;
import org.jooq.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.row;


public class TicketConditionBuilder {

    public static List<Condition> build(
            UUID accountId,
            TicketStatus status,
            TicketSearchCursor cursor,
            Ticket ticket,
            Booking booking
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(booking.ACCOUNT_ID.eq(accountId));

        if (status != null) {
            conditions.add(ticket.STATUS.eq(status.name()));
        }

        if(cursor != null) {
            conditions.add(
                    row(ticket.CREATED_AT, ticket.ID)
                            .gt(row(cursor.primaryValue(), cursor.ticketId()))
            );
        }

        return conditions;
    }

}
