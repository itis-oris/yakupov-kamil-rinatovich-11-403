package com.arsaka.auth.request;

public record ValidateRequest(
        String token,
        String path,
        String method
) {
}
