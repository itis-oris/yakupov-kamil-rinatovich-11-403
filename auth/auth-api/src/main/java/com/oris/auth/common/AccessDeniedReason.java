package com.oris.auth.common;

public enum AccessDeniedReason {
    TOKEN_EXPIRED(401, "token expired"),
    TOKEN_INVALID(401, "token invalid");

    private final int code;
    private final String message;

    AccessDeniedReason(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
