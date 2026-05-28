package com.oris.auth.request;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 2, max = 72)
        String username,

        @NotBlank
        @Size(min = 8, max = 72)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Password must contain: uppercase, lowercase and digit"
        )
        String password
) {}
