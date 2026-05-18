package com.arsaka.flightsearch.service;

import com.arsaka.booking.dto.TicketRecord;
import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
import com.arsaka.create.request.CreateFlightInventoryRequest;
import com.arsaka.create.response.FlightInventoryResponse;
import com.arsaka.flightsearch.exception.FlightInventoryAlreadyExistsException;
import com.arsaka.flightsearch.exception.FlightInventoryNotFoundException;
import com.arsaka.flightsearch.exception.FlightInventoryHoldException;
import com.arsaka.flightsearch.mapper.FlightInventoryMapper;
import com.arsaka.flightsearch.model.Flight;
import com.arsaka.flightsearch.model.FlightInventory;
import com.arsaka.flightsearch.repository.FlightInventoryCommandRepository;
import com.arsaka.flightsearch.repository.FlightInventoryQueryRepository;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.updaterequest.UpdateFlightInventoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightInventoryService {

    private final FlightInventoryQueryRepository queryRepository;
    private final FlightInventoryCommandRepository commandRepository;
    private final FlightService flightService;
    private final FlightInventoryMapper mapper;

    public BigDecimal getPrice(UUID flightId, CabinClass cabinClass) {
        BigDecimal price = queryRepository.getPrice(flightId, cabinClass);

        if (price == null) {
            throw new FlightInventoryNotFoundException(flightId, cabinClass);
        }

        return price;
    }

    public void holdSeats(Set<FlightHoldDto> flights) {
        for(FlightHoldDto flight: flights) {
            if(flight.getPassengerType() != PassengerType.INFANT) {
                if(!queryRepository.setHeld(flight.getFlightId(), flight.getFareCabinClass())) {
                    throw new FlightInventoryHoldException(flight.getFlightId(), flight.getFareCabinClass());
                }
            }
        }

    }

    public void releaseHold(List<TicketRecord> tickets) {
        for (TicketRecord ticket: tickets) {
            if(ticket.getPassengerType() != PassengerType.INFANT) {
                queryRepository.releaseHold(ticket.getFlightId(), ticket.getCabinClass());
            }
        }
    }

    @Transactional
    public FlightInventoryResponse create(CreateFlightInventoryRequest request) {
        Flight flight = flightService.findById(request.flightId());

        if(commandRepository.existsByFlightAndCabinClass(flight, request.cabinClass())) {
            throw new FlightInventoryAlreadyExistsException(request.flightId(), request.cabinClass());
        }

        FlightInventory flightInventory = mapper.toEntity(request, flight);
        flightInventory.setTotalSeats(flight.getAirplaneType().getTotalSeats());
        flightInventory.setAvailableSeats(flight.getAirplaneType().getTotalSeats());

        FlightInventory saved = commandRepository.save(flightInventory);

        return mapper.toResponse(saved);
    }

    @Transactional
    public FlightInventoryResponse update(UUID id, UpdateFlightInventoryRequest request) {
        FlightInventory flightInventory = commandRepository.findById(id)
                .orElseThrow(() -> new FlightInventoryNotFoundException(id));

        flightInventory.setPrice(request.price());

        return mapper.toResponse(flightInventory);
    }
}
