package com.oris.auth.controller;

import com.oris.auth.request.UpdateUsernameRequest;
import com.oris.auth.response.AccountResponse;
import com.oris.auth.service.AuthServiceClient;
import com.oris.controller.AccountApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountProxyController implements AccountApi {

    private final AuthServiceClient authServiceClient;

    @Override
    public ResponseEntity<AccountResponse> updateUsername(UUID accountId, UpdateUsernameRequest request) {
        AccountResponse updated = authServiceClient.updateUsername(accountId, request);
        return ResponseEntity.ok(updated);
    }
}
