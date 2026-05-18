package com.arsaka.auth.exception;

import java.util.UUID;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(UUID accountId) {
        super("Account not active: %s".formatted(accountId));
    }
}
