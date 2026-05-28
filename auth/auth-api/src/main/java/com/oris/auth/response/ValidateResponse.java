package com.oris.auth.response;

import com.oris.auth.common.AccessDeniedReason;
import com.oris.auth.common.AccountRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidateResponse(
        boolean allowed,
        UUID accountId,
        List<AccountRole> roles,
        AccessDeniedReason reason
) {
    public static ValidateResponse ofAllowed(UUID accountId, List<AccountRole> roles) {
        return new ValidateResponse(true, accountId, roles, null);
    }

    public static ValidateResponse ofNotAllowed(AccessDeniedReason reason) {
        return new ValidateResponse(false, null, null, reason);
    }
}
