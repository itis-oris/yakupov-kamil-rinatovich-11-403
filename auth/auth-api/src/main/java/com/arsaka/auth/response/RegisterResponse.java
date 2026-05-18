package com.arsaka.auth.response;

import com.arsaka.auth.common.AccountRole;

import java.util.List;
import java.util.UUID;

public record RegisterResponse(
        UUID accountId,
        String email,
        List<AccountRole> roles
) {}
