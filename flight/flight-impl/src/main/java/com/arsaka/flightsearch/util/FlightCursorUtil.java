package com.arsaka.flightsearch.util;

import com.arsaka.flightsearch.exception.CursorDecodeException;
import com.arsaka.flightsearch.dto.FlightSearchRecord;
import com.arsaka.search.request.dto.OrderType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
public class FlightCursorUtil {

    public static final int cursorLimit = 1;
    private static final String SEPARATOR_CHAR = ";";

    public static FlightSearchCursor decode(String cursorData, OrderType orderType) {
        if (cursorData == null || cursorData.isBlank()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cursorData);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] cursorParts = decodedString.split(SEPARATOR_CHAR);
            return switch (orderType) {
                case DEPARTURE_ASC -> new FlightSearchCursor(
                        orderType,
                        Instant.parse(cursorParts[0]),
                        UUID.fromString(cursorParts[1])
                );
                case PRICE_ASC, PRICE_DESC -> new FlightSearchCursor(
                        orderType,
                        new BigDecimal(cursorParts[0]),
                        UUID.fromString(cursorParts[1])
                );
            };
        } catch (Exception e) {
            log.error("failed to decode cursor exception | message={} | cursor={}", e.getMessage(), cursorData);
            throw new CursorDecodeException();
        }

    }

    public static String getNext(List<FlightSearchRecord> flights, OrderType orderType) {

        if(flights.isEmpty()) {
            return null;
        }

        FlightSearchRecord last =  flights.get(flights.size() - 1);

        if (last == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch (orderType) {
            case DEPARTURE_ASC -> sb.append(last.getScheduledDeparture());
            case PRICE_ASC, PRICE_DESC -> sb.append(last.getPrice().toPlainString());
        }
        sb.append(SEPARATOR_CHAR).append(last.getFlightId());
        byte[] data = sb.toString().getBytes();

        return  Base64.getEncoder().encodeToString(data);
    }

}
