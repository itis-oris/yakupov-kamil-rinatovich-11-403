package com.arsaka.create.request;

import com.arsaka.common.CabinClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateFareRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{2}$")
        String airlineCode,

        @NotNull
        CabinClass cabinClass,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotNull
        Boolean baggageIncluded,

        @NotNull
        Boolean isRefundable
) {
}
