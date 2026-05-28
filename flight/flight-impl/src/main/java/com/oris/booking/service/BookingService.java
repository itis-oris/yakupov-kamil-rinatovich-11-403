package com.oris.booking.service;

import com.oris.booking.exception.BookingAccessDeniedException;
import com.oris.booking.exception.BookingDuplicateException;
import com.oris.booking.exception.BookingNotFoundException;
import com.oris.booking.exception.BookingReleaseHoldException;
import com.oris.booking.model.BookingStatus;
import com.oris.booking.repository.BookingRepository;
import com.oris.booking.dto.FlightHoldDto;
import com.oris.jooq.tables.records.BookingRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository repository;

    public void save(UUID bookingId, UUID accountId, Set<FlightHoldDto> flights) {
        BigDecimal totalPrice = new BigDecimal(0);
        for(FlightHoldDto flight: flights) {
            totalPrice = totalPrice.add(flight.getTotalPrice());
        }

        if(!repository.save(bookingId, totalPrice, accountId)) {
            log.debug("booking already exists exception | bookingId={}", bookingId);
            throw new BookingDuplicateException();
        }
    }

    public void releaseHold(UUID bookingId) {
        BookingRecord bookingRecord = repository.findById(bookingId);

        if(bookingRecord == null) {
            log.debug("booking not found exception | bookingId={}", bookingId);
            throw new BookingNotFoundException();
        }

        if(bookingRecord.getStatus().equals(BookingStatus.CONFIRMED.name())) {
            log.debug("booking already confirmed exception | bookingId={}", bookingId);
            throw new BookingReleaseHoldException();
        }

        repository.setStatus(bookingId, BookingStatus.CANCELLED);
    }

    public void checkBookingAccountId(UUID accountId, UUID bookingId) {
        BookingRecord bookingRecord = repository.findById(bookingId);
        if(!accountId.equals(bookingRecord.getAccountId())) {
            log.debug("Access denied to booking exception | account id={} | booking id={}", accountId, bookingId);
            throw new BookingAccessDeniedException();
        }
    }

    public void confirm(UUID bookingId) {
        repository.setStatus(bookingId, BookingStatus.CONFIRMED);
    }
}
