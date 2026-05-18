package com.arsaka.create.request;

import jakarta.validation.constraints.*;

public record CreateAirplaneRequest(
        @NotBlank
        @Size(max = 100)
        String number,

        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{3}$")
        String airplaneTypeCode,

        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{2}$")
        String airlineCode,

        @NotNull
        @Min(1900)
        @Max(2100)
        Integer manufacturedYear
) {
}
