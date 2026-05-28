package com.oris.controller;

import com.oris.create.request.CreatePricingAdjustmentRequest;
import com.oris.create.response.PricingAdjustmentResponse;
import com.oris.search.response.AdminPage;
import com.oris.updaterequest.UpdatePricingAdjustmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin pricing adjustments",
        description = "API for pricing adjustments admin management: create, update, delete, get list")
@RestController
@RequestMapping("/api/v1/admin/flights/pricing/adjustments")
public interface PricingAdjustmentApi {

    @Operation(summary = "Create new Pricing Adjustment")
    @PostMapping
    ResponseEntity<PricingAdjustmentResponse> create(
            @Valid @RequestBody CreatePricingAdjustmentRequest request
    );

    @Operation(summary = "Update Pricing Adjustment")
    @PatchMapping("/{id}")
    ResponseEntity<PricingAdjustmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePricingAdjustmentRequest request
    );

    @Operation(summary = "Delete Pricing Adjustment")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable UUID id
    );

    @Operation(summary = "Get pricing adjustments list")
    @GetMapping
    ResponseEntity<AdminPage<PricingAdjustmentResponse>> listAdjustments(
            @RequestParam(required = false) UUID flightId,
            @RequestParam(required = false) UUID fareId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
