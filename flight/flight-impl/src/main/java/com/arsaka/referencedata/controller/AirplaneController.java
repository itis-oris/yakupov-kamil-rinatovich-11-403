package com.arsaka.referencedata.controller;

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
@RequestMapping("/api/v1/admin/flights/airplanes")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AirplaneController {

    private final AirplaneService service;

    @PostMapping
    public ResponseEntity<AirplaneResponse> create(
            @Valid @RequestBody CreateAirplaneRequest request
    ) {
        AirplaneResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
