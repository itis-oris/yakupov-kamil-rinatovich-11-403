package com.arsaka.auth.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException() {
        super("Account not active");
    }
}
