package com.arsaka.create.request;

import com.arsaka.create.request.dto.Time;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateFlightRequest(
        @NotNull
        UUID routeId,

        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{3}$")
        String airplaneTypeCode,

        @NotNull
        UUID airplaneId,

        @NotNull
        @Valid
        Time scheduledTime
) {
}
