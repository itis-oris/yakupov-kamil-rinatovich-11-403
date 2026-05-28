package com.oris.flight.service;

import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.booking.dto.TicketRecord;
import com.oris.common.CabinClass;
import com.oris.common.PassengerType;
import com.oris.create.request.CreateFlightInventoryRequest;
import com.oris.create.response.FlightInventoryResponse;
import com.oris.flight.exception.FlightInventoryAlreadyExistsException;
import com.oris.flight.exception.FlightInventoryNotFoundException;
import com.oris.flight.exception.FlightInventoryHoldException;
import com.oris.flight.mapper.FlightInventoryMapper;
import com.oris.flight.model.Flight;
import com.oris.flight.model.FlightInventory;
import com.oris.flight.repository.FlightInventoryCommandRepository;
import com.oris.flight.repository.FlightInventoryQueryRepository;
import com.oris.booking.dto.FlightHoldDto;
import com.oris.updaterequest.UpdateFlightInventoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightInventoryService {

    private final FlightInventoryQueryRepository queryRepository;
    private final FlightInventoryCommandRepository commandRepository;
    private final FlightService flightService;
    private final FlightInventoryMapper mapper;

    public BigDecimal getPrice(UUID flightId, CabinClass cabinClass) {
        BigDecimal price = queryRepository.getPrice(flightId, cabinClass);

        if (price == null) {
            log.debug("flight inventory not found exception | flight id={} | cabin class={}", flightId, cabinClass);
            throw new FlightInventoryNotFoundException();
        }

        return price;
    }

    public void holdSeats(Set<FlightHoldDto> flights) {
        for(FlightHoldDto flight: flights) {
            if(flight.getPassengerType() != PassengerType.INFANT) {
                if(!queryRepository.setHeld(flight.getFlightId(), flight.getFareCabinClass())) {
                    log.debug("Flight Inventory is not available exception | flightId={} | cabinClass={}", flight.getFlightId(), flight.getFareCabinClass());
                    throw new FlightInventoryHoldException();
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
            log.debug("Flight inventory already exists exception | flight id={} | cabin class={}", request.flightId(), request.cabinClass());
            throw new FlightInventoryAlreadyExistsException();
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
                .orElseThrow(() -> {
                    log.debug("flight inventory not found exception | flight inventory id={}", id);
                    return new FlightInventoryNotFoundException();
                });

        flightInventory.setPrice(request.price());

        return mapper.toResponse(flightInventory);
    }

    @Transactional
    public AdminPage<FlightInventoryResponse> findInventories(
            UUID flightId,
            AdminPageRequest pageReq
    ) {
        Specification<FlightInventory> spec = Specification.where(null);

        if (flightId != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("flight").get("id"), flightId));
        }

        Page<FlightInventory> page = commandRepository.findAll(
                spec,
                PageRequest.of(pageReq.page(), pageReq.size(),
                        Sort.by("createdAt"))
        );

        List<FlightInventoryResponse> content = page.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return AdminPage.of(content, pageReq, page.getTotalElements());
    }
}
