package com.arsaka.auth.dto;

import com.arsaka.auth.common.AccountRole;

import java.util.List;
import java.util.UUID;

public record AccountAuthData(
        UUID accountId,
        List<AccountRole> roles
) {}
