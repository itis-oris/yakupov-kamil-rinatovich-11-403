package com.arsaka.pricing.controller;

import com.arsaka.controller.PricingAdjustmentApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.create.request.CreatePricingAdjustmentRequest;
import com.arsaka.create.response.PricingAdjustmentResponse;
import com.arsaka.pricing.service.PricingAdjustmentService;
import com.arsaka.updaterequest.UpdatePricingAdjustmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PricingAdjustmentController implements PricingAdjustmentApi {

    private final PricingAdjustmentService service;

    @Override
    public ResponseEntity<PricingAdjustmentResponse> create(CreatePricingAdjustmentRequest request) {
        PricingAdjustmentResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<PricingAdjustmentResponse> update(UUID id, UpdatePricingAdjustmentRequest request) {
        PricingAdjustmentResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdminPage<PricingAdjustmentResponse>> listAdjustments(UUID flightId, UUID fareId, Integer page, Integer size) {
        AdminPage<PricingAdjustmentResponse> response = service.findAdjustments(flightId, fareId, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
