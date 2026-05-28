package com.oris.auth.dto;

import com.oris.auth.common.AccountRole;

import java.util.List;
import java.util.UUID;

public record AccountAuthData(
        UUID accountId,
        List<AccountRole> roles
) {}
