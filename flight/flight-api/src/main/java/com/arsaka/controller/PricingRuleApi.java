package com.arsaka.controller;

import com.arsaka.create.request.CreatePricingRuleRequest;
import com.arsaka.create.response.PricingRuleResponse;
import com.arsaka.search.response.AdminPage;
import com.arsaka.updaterequest.UpdatePricingRuleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin pricing rules",
        description = "API for pricing rules admin management: create, update, delete, get list")
@RestController
@RequestMapping("/api/v1/admin/flights/pricing/rules")
public interface PricingRuleApi {

    @Operation(summary = "Create new Pricing Rule")
    @PostMapping
    ResponseEntity<PricingRuleResponse> create(
            @Valid @RequestBody CreatePricingRuleRequest request
    );

    @Operation(summary = "Update Pricing Rule")
    @PatchMapping("/{id}")
    ResponseEntity<PricingRuleResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePricingRuleRequest request
    );

    @Operation(summary = "Delete Pricing Rule")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable UUID id
    );

    @Operation(summary = "Get pricing rules list")
    @GetMapping
    ResponseEntity<AdminPage<PricingRuleResponse>> listRules(
            @RequestParam(required = false) UUID fareId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    );
}
