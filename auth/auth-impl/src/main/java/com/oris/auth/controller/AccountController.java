package com.oris.auth.controller;

import com.oris.auth.request.UpdateUsernameRequest;
import com.oris.auth.response.AccountResponse;
import com.oris.auth.service.AuthOrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {

    private final AuthOrchestrationService service;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> get(@PathVariable UUID accountId) {
        AccountResponse response = service.findAccountById(accountId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateUsername(
            @RequestBody UpdateUsernameRequest usernameRequest,
            @PathVariable UUID accountId
    ) {
        AccountResponse response = service.updateUsername(accountId, usernameRequest.username());
        return ResponseEntity.ok(response);
    }

}
