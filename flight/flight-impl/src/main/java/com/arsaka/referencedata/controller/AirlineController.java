package com.arsaka.referencedata.controller;

import com.arsaka.controller.AirlineApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.create.request.CreateAirlineRequest;
import com.arsaka.create.response.AirlineResponse;
import com.arsaka.referencedata.service.AirlineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
