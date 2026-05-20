package com.arsaka.booking.util;

import com.arsaka.booking.dto.TicketSearchRecord;
import com.arsaka.flightsearch.exception.CursorDecodeException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
public class TicketCursorUtil {

    public static final int cursorLimit = 5;
    private static final String SEPARATOR_CHAR = ";";

    public static TicketSearchCursor decode(String cursorData) {
        if (cursorData == null || cursorData.isBlank()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cursorData);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] cursorParts = decodedString.split(SEPARATOR_CHAR);
            return new TicketSearchCursor(
                    Instant.parse(cursorParts[0]),
                    UUID.fromString(cursorParts[1])
                );
        } catch (Exception e) {
            log.error("failed to decode cursor exception | message={} | cursor={}", e.getMessage(), cursorData);
            throw new CursorDecodeException();
        }

    }

    public static String getNext(List<TicketSearchRecord> tickets) {

        if(tickets.isEmpty()) {
            return null;
        }

        TicketSearchRecord last = tickets.get(tickets.size() - 1);

        if (last == null) {
            return null;
        }
        byte[] data = (last.getCreatedAt() + SEPARATOR_CHAR + last.getId()).getBytes();

        return  Base64.getEncoder().encodeToString(data);
    }
}
