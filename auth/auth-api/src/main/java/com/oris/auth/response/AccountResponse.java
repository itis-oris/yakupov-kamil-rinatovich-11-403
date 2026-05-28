package com.oris.auth.response;

import com.oris.auth.common.AccountStatus;

import java.time.Instant;

public record AccountResponse(
        String username,
        String email,
        Instant createdAt,
        AccountStatus status
) {
}
