package com.arsaka.pricing.controller;

import com.arsaka.create.request.CreateFareRequest;
import com.arsaka.create.response.FareResponse;
import com.arsaka.pricing.service.FareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/flights/fares")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FareController {

    private final FareService service;

    @PostMapping
    public ResponseEntity<FareResponse> create(
            @Valid @RequestBody CreateFareRequest request
    ) {
        FareResponse response = service.create(request);
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
