package com.arsaka.pricing.controller;

import com.arsaka.create.request.CreatePricingRuleRequest;
import com.arsaka.create.response.PricingRuleResponse;
import com.arsaka.pricing.service.PricingRuleService;
import com.arsaka.updaterequest.UpdatePricingRuleRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/flights/pricing/rules")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PricingRuleController {

    private final PricingRuleService service;

    @PostMapping
    public ResponseEntity<PricingRuleResponse> create(
            @Valid @RequestBody CreatePricingRuleRequest request
    ) {
        PricingRuleResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PricingRuleResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePricingRuleRequest request
    ) {
        PricingRuleResponse response = service.update(id, request);
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
