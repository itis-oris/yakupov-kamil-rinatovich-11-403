package com.arsaka.auth.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiException(
        int status,
        String detail,
        String path,
        Instant timestamp,
        Map<String, List<String>> errors
) {
    public static ApiException of(int status, String detail, String path) {
        return new ApiException(status, detail, path, Instant.now(), null);
    }

    public static ApiException ofValidation(int status, Map<String, List<String>> errors, String path) {
        return new ApiException( status, null, path, Instant.now(), errors);
    }
}
