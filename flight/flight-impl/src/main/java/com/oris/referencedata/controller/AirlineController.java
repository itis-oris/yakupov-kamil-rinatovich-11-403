package com.oris.referencedata.controller;

import com.oris.controller.AirlineApi;
import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.create.request.CreateAirlineRequest;
import com.oris.create.response.AirlineResponse;
import com.oris.referencedata.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AirlineController implements AirlineApi {

    private final AirlineService service;

    @Override
    public ResponseEntity<AirlineResponse> create(CreateAirlineRequest request) {
        AirlineResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> delete(String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdminPage<AirlineResponse>> listAirlines(String countryCode, Boolean active, Integer page, Integer size) {
        AdminPage<AirlineResponse> response =  service.findAirlines(countryCode, active, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
