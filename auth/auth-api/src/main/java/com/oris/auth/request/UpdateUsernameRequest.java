package com.oris.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUsernameRequest(
        @NotBlank
        @Size(min = 2, max = 72)
        String username
) {}