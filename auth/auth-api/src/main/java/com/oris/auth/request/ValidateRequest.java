package com.oris.auth.request;

public record ValidateRequest(
        String token,
        String path,
        String method
) {
}
