package com.arsaka.auth.response;

import com.arsaka.auth.common.AccountStatus;

import java.time.Instant;

public record AccountResponse(
        String username,
        String email,
        Instant createdAt,
        AccountStatus status
) {
}
