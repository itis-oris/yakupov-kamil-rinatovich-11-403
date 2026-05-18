package com.arsaka.referencedata.service;

import com.arsaka.common.CabinClass;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.search.response.dto.FlightSeat;
import com.arsaka.common.PassengerType;
import com.arsaka.referencedata.exception.SeatNotFoundException;
import com.arsaka.referencedata.exception.SeatHoldException;
import com.arsaka.referencedata.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository repository;

    public Set<FlightSeat> getSeats(UUID flightId) {
        return repository.getSeats(flightId);
    }

    public FlightSeat getSeat(UUID flightId, UUID seatId) {
        FlightSeat seat = repository.getSeat(flightId, seatId);

        if (seat == null) {
            throw new SeatNotFoundException(seatId);
        }

        return seat;
    }

    public void holdSeats(UUID bookingId, Set<FlightHoldDto> flights) {
        for(FlightHoldDto flight: flights) {
            if(flight.getPassengerType() != PassengerType.INFANT) {
                if(!repository.hold(flight.getFlightId(), flight.getSeatId(), bookingId)) {
                    throw new SeatHoldException(flight.getFlightId(), flight.getSeatId());
                }
            }
        }
    }

    public CabinClass getCabinClass(UUID seatId) {
        CabinClass cabinClass = repository.getCabinClass(seatId);

        if (cabinClass == null) {
            throw new SeatNotFoundException(seatId);
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
