package com.arsaka.referencedata.controller;

import com.arsaka.controller.AirplaneApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.create.request.CreateAirplaneRequest;
import com.arsaka.create.response.AirplaneResponse;
import com.arsaka.referencedata.service.AirplaneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
