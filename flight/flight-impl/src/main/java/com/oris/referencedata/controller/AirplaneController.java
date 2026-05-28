package com.oris.referencedata.controller;

import com.oris.controller.AirplaneApi;
import com.oris.search.response.AdminPage;
import com.oris.search.request.dto.AdminPageRequest;
import com.oris.create.request.CreateAirplaneRequest;
import com.oris.create.response.AirplaneResponse;
import com.oris.referencedata.service.AirplaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AirplaneController implements AirplaneApi {

    private final AirplaneService service;

    @Override
    public ResponseEntity<AirplaneResponse> create(CreateAirplaneRequest request) {
        AirplaneResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdminPage<AirplaneResponse>> listAirplanes(String airlineCode, String airplaneTypeCode, Integer page, Integer size) {
        AdminPage<AirplaneResponse> response = service.findAirplanes(airlineCode, airplaneTypeCode, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
