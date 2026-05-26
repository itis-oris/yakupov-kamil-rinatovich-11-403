package com.arsaka.pricing.controller;

import com.arsaka.controller.FareApi;
import com.arsaka.search.response.AdminPage;
import com.arsaka.search.request.dto.AdminPageRequest;
import com.arsaka.common.CabinClass;
import com.arsaka.create.request.CreateFareRequest;
import com.arsaka.create.response.FareResponse;
import com.arsaka.pricing.service.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FareController implements FareApi {

    private final FareService service;

    @Override
    public ResponseEntity<FareResponse> create(CreateFareRequest request) {
        FareResponse response = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdminPage<FareResponse>> listFares(String airlineCode, CabinClass cabinClass, Integer page, Integer size) {
        AdminPage<FareResponse> response = service.findFares(airlineCode, cabinClass, AdminPageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
