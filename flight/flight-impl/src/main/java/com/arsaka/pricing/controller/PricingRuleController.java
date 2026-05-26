package com.arsaka.pricing.controller;

import com.arsaka.controller.PricingRuleApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
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
@RequiredArgsConstructor
public class PricingRuleController implements PricingRuleApi {

    private final PricingRuleService service;

    @Override
    public ResponseEntity<PricingRuleResponse> create(CreatePricingRuleRequest request) {
        PricingRuleResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<PricingRuleResponse> update(UUID id, UpdatePricingRuleRequest request) {
        PricingRuleResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdminPage<PricingRuleResponse>> listRules(UUID fareId, Integer page, Integer size) {
        AdminPage<PricingRuleResponse> response = service.findRules(fareId, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
