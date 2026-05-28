package com.oris.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FlightPassengerDocument(
        @NotNull
        DocumentType type,

        @NotBlank
        String number,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{2}$")
        String countryCode
) {
}
