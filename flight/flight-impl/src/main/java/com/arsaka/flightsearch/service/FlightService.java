package com.arsaka.flightsearch.service;

import com.arsaka.common.CabinClass;
import com.arsaka.create.request.CreateFlightRequest;
import com.arsaka.create.response.FlightResponse;
import com.arsaka.flightsearch.dto.FlightRecord;
import com.arsaka.flightsearch.dto.FlightSearchDto;
import com.arsaka.flightsearch.dto.FlightSearchRecord;
import com.arsaka.flightsearch.exception.AirplaneTypeMismatchException;
import com.arsaka.flightsearch.exception.FlightNotFoundException;
import com.arsaka.flightsearch.mapper.FlightMapper;
import com.arsaka.flightsearch.model.Flight;
import com.arsaka.flightsearch.model.Route;
import com.arsaka.flightsearch.repository.FlightCommandRepository;
import com.arsaka.referencedata.model.Airplane;
import com.arsaka.referencedata.model.AirplaneType;
import com.arsaka.referencedata.service.AirplaneService;
import com.arsaka.referencedata.service.AirplaneTypeService;
import com.arsaka.search.request.FlightsSearchRequest;
import com.arsaka.flightsearch.exception.FlightInventoryNotFoundException;
import com.arsaka.flightsearch.util.FlightCursorUtil;
import com.arsaka.flightsearch.util.FlightSearchCursor;
import com.arsaka.flightsearch.repository.FlightQueryRepository;
import com.arsaka.updaterequest.UpdateFlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
            throw new FlightInventoryNotFoundException(flightId, cabinClass);
        }

        return record;
    }

    public Flight findById(UUID id) {
        return commandRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));
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

    private void setFieldsToUpdate(UpdateFlightRequest request, Flight flight) {
        if (request.airplaneId() != null) {
            Airplane airplane = airplaneService.findById(request.airplaneId());
            if(!airplane.getAirplaneType().equals(flight.getAirplaneType())) {
                throw new AirplaneTypeMismatchException(
                        airplane.getAirplaneType().getCode(),
                        flight.getAirplaneType().getCode()
                );
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
