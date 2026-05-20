package com.arsaka.flight.service;

import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.common.CabinClass;
import com.arsaka.common.FlightStatus;
import com.arsaka.create.request.CreateFlightRequest;
import com.arsaka.create.response.FlightResponse;
import com.arsaka.flight.dto.FlightRecord;
import com.arsaka.flight.dto.FlightSearchDto;
import com.arsaka.flight.dto.FlightSearchRecord;
import com.arsaka.flight.exception.AirplaneTypeMismatchException;
import com.arsaka.flight.exception.FlightNotFoundException;
import com.arsaka.flight.mapper.FlightMapper;
import com.arsaka.flight.model.Flight;
import com.arsaka.flight.model.Route;
import com.arsaka.flight.repository.FlightCommandRepository;
import com.arsaka.referencedata.model.Airplane;
import com.arsaka.referencedata.model.AirplaneType;
import com.arsaka.referencedata.service.AirplaneService;
import com.arsaka.referencedata.service.AirplaneTypeService;
import com.arsaka.search.request.FlightsSearchRequest;
import com.arsaka.flight.exception.FlightInventoryNotFoundException;
import com.arsaka.flight.util.FlightCursorUtil;
import com.arsaka.flight.util.FlightSearchCursor;
import com.arsaka.flight.repository.FlightQueryRepository;
import com.arsaka.updaterequest.UpdateFlightRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {

    private final FlightQueryRepository queryRepository;
    private final FlightCommandRepository commandRepository;
    private final RouteService routeService;
    private final AirplaneTypeService airplaneTypeService;
    private final AirplaneService airplaneService;
    private final FlightMapper mapper;

    public List<FlightSearchRecord> get(FlightsSearchRequest request, FlightSearchDto dto) {

        FlightSearchCursor cursor = FlightCursorUtil.decode(request.cursor(), dto.getOrderType());

        return queryRepository.findFlights(
                dto,
                request.filter(),
                cursor,
                FlightCursorUtil.cursorLimit
        );
    }

    public FlightRecord get(UUID flightId, CabinClass cabinClass) {
        FlightRecord record = queryRepository.findFlight(flightId, cabinClass);

        if (record == null) {
            log.debug("flight inventory not found exception | flight id={} | cabin class={}", flightId, cabinClass);
            throw new FlightInventoryNotFoundException();
        }

        return record;
    }

    public Flight findById(UUID id) {
        return commandRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("flight not found exception | flight id={}", id);
                    return new FlightNotFoundException();}
                );
    }

    @Transactional
    public FlightResponse create(CreateFlightRequest request) {
        Route route = routeService.findById(request.routeId());

        AirplaneType airplaneType = airplaneTypeService.findByCode(request.airplaneTypeCode());

        Airplane airplane = airplaneService.findById(request.airplaneId());

        Flight flight = mapper.toEntity(request, route, airplaneType, airplane);

        Flight saved = commandRepository.save(flight);

        return mapper.toResponse(saved);
    }

    @Transactional
    public FlightResponse update(UUID id, UpdateFlightRequest request) {
        Flight flight = findById(id);

        setFieldsToUpdate(request, flight);

        return mapper.toResponse(flight);
    }

    @Transactional
    public AdminPage<FlightResponse> findFlights(
            UUID routeId,
            FlightStatus status,
            AdminPageRequest pageReq
    ) {
        Specification<Flight> spec = Specification.where(null);

        if (routeId != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("route").get("id"), routeId));
        }

        if (status != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("status"), status));
        }

        Page<Flight> page = commandRepository.findAll(
                spec,
                PageRequest.of(pageReq.page(), pageReq.size(),
                        Sort.by(Sort.Direction.DESC, "scheduledDeparture"))
        );

        List<FlightResponse> content = page.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return AdminPage.of(content, pageReq, page.getTotalElements());
    }

    private void setFieldsToUpdate(UpdateFlightRequest request, Flight flight) {
        if (request.airplaneId() != null) {
            Airplane airplane = airplaneService.findById(request.airplaneId());
            if(!airplane.getAirplaneType().equals(flight.getAirplaneType())) {
                log.debug("Assigned airplane type does not match flight airplane type exception | assigned airplane type code={} | required airplane type code={}",
                        airplane.getAirplaneType().getCode(),
                        flight.getAirplaneType().getCode());
                throw new AirplaneTypeMismatchException();
            }
            flight.setAirplane(airplane);
        }

        if(request.scheduledTime() != null) {
            if(request.scheduledTime().departure() != null) {
                flight.setScheduledDeparture(request.scheduledTime().departure());
            }

            if(request.scheduledTime().arrival() != null) {
                flight.setScheduledArrival(request.scheduledTime().arrival());
            }
        }

        if(request.actualTime() != null) {
            if(request.actualTime().departure() != null) {
                flight.setActualDeparture(request.actualTime().departure());
            }

            if(request.actualTime().arrival() != null) {
                flight.setActualArrival(request.actualTime().arrival());
            }
        }

        if(request.status() != null) {
            flight.setStatus(request.status());
        }
    }

}
