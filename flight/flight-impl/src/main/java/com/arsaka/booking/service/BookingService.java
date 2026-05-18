package com.arsaka.booking.service;

import com.arsaka.booking.exception.BookingDuplicateException;
import com.arsaka.booking.exception.BookingNotFoundException;
import com.arsaka.booking.exception.BookingReleaseHoldException;
import com.arsaka.booking.model.BookingStatus;
import com.arsaka.booking.repository.BookingRepository;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.jooq.tables.records.BookingRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repository;

    public void save(UUID bookingId, UUID accountId, Set<FlightHoldDto> flights) {
        BigDecimal totalPrice = new BigDecimal(0);
        for(FlightHoldDto flight: flights) {
            totalPrice = totalPrice.add(flight.getTotalPrice());
        }

        if(!repository.save(bookingId, totalPrice, accountId)) {
            throw new BookingDuplicateException(bookingId);
        }
    }

    public void releaseHold(UUID bookingId) {
        BookingRecord bookingRecord = repository.findById(bookingId);

        if(bookingRecord == null) {
            throw new BookingNotFoundException(bookingId);
        }

        if(bookingRecord.getStatus().equals(BookingStatus.CONFIRMED.name())) {
            throw new BookingReleaseHoldException(bookingId);
        }

        repository.setStatus(bookingId, BookingStatus.CANCELLED);
    }

    public void confirm(UUID bookingId) {
        repository.setStatus(bookingId, BookingStatus.CONFIRMED);
    }
}
