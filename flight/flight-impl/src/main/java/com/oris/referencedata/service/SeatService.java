package com.oris.referencedata.service;

import com.oris.common.CabinClass;
import com.oris.booking.dto.FlightHoldDto;
import com.oris.config.CacheName;
import com.oris.search.response.dto.FlightSeat;
import com.oris.common.PassengerType;
import com.oris.referencedata.exception.SeatNotFoundException;
import com.oris.referencedata.exception.SeatHoldException;
import com.oris.referencedata.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheName.SEAT_LIST)
@Slf4j
public class SeatService {

    private final SeatRepository repository;

    @Cacheable
    public Set<FlightSeat> getSeats(UUID flightId) {
        return repository.getSeats(flightId);
    }

    public FlightSeat getSeat(UUID flightId, UUID seatId) {
        if(seatId == null) {
            return null;
        }

        FlightSeat seat = repository.getSeat(flightId, seatId);

        if (seat == null) {
            log.debug("Seat not found exception | seatId={}", seatId);
            throw new SeatNotFoundException();
        }

        return seat;
    }

    public void holdSeats(UUID bookingId, Set<FlightHoldDto> flights) {
        for(FlightHoldDto flight: flights) {
            if(flight.getPassengerType() != PassengerType.INFANT) {
                if(!repository.hold(flight.getFlightId(), flight.getSeatId(), bookingId)) {
                    log.debug("Seat is not available exception | flightId={} | seatId={}",flight.getFlightId(), flight.getSeatId());
                    throw new SeatHoldException();
                }
            }
        }
    }

    public CabinClass getCabinClass(UUID seatId) {
        CabinClass cabinClass = repository.getCabinClass(seatId);

        if (cabinClass == null) {
            log.debug("Seat not found exception | seatId={}", seatId);
            throw new SeatNotFoundException();
        }

        return cabinClass;
    }

    public void releaseHold(UUID bookingId) {
        repository.releaseHold(bookingId);
    }

    public void reserve(UUID bookingId) {
        repository.reserve(bookingId);
    }
}
