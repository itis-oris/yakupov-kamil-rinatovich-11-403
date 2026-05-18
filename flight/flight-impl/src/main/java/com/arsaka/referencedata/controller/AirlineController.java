package com.arsaka.referencedata.controller;

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
@RequestMapping("/api/v1/admin/flights/airlines")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AirlineController {

    private final AirlineService service;

    @PostMapping
    public ResponseEntity<AirlineResponse> create(
            @Valid @RequestBody CreateAirlineRequest request
    ) {
        AirlineResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotBlank
            @Pattern(regexp = "^[A-Z0-9]{2}$")
            String code
    ) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }
}
