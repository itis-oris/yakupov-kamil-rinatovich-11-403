package com.arsaka.event.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FlightPassenger(
        @NotBlank
        @Pattern(regexp = "^\\p{L}[\\p{L} \\-']{0,48}\\p{L}$")
        String firstName,

        @NotBlank
        @Pattern(regexp = "^\\p{L}[\\p{L} \\-']{0,48}\\p{L}$")
        String lastName,

        @NotNull
        @Past
        LocalDate dateOfBirth,

        @NotNull
        PassengerGender gender,

        @NotNull
        @Valid
        FlightPassengerDocument document
) {
}
