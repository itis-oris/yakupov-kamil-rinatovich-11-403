package com.oris.create.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAirlineRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{2}$")
        String code,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{2}$")
        String countryCode
) {
}
