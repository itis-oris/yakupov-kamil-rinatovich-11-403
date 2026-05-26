package com.arsaka.auth.controller;

import com.arsaka.auth.request.UpdateUsernameRequest;
import com.arsaka.auth.response.AccountResponse;
import com.arsaka.auth.service.AuthServiceClient;
import com.arsaka.controller.AccountApi;
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
