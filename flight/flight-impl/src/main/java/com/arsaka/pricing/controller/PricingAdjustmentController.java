package com.arsaka.pricing.controller;

import com.arsaka.create.request.CreatePricingAdjustmentRequest;
import com.arsaka.create.response.PricingAdjustmentResponse;
import com.arsaka.pricing.service.PricingAdjustmentService;
import com.arsaka.updaterequest.UpdatePricingAdjustmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/flights/pricing/adjustments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PricingAdjustmentController {

    private final PricingAdjustmentService service;

    @PostMapping
    public ResponseEntity<PricingAdjustmentResponse> create(
            @Valid @RequestBody CreatePricingAdjustmentRequest request
    ) {
        PricingAdjustmentResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PricingAdjustmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePricingAdjustmentRequest request
    ) {
        PricingAdjustmentResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
