package com.arsaka.controller;

import com.arsaka.auth.request.UpdateUsernameRequest;
import com.arsaka.auth.response.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Account",
        description = "API for account management: update username")
@RestController
@RequestMapping("/api/v1/account")
public interface AccountApi {

    @Operation(summary = "Update username")
    @PatchMapping("/me/username")
    ResponseEntity<AccountResponse> updateUsername(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UUID accountId,
            @RequestBody UpdateUsernameRequest request
    );
}
