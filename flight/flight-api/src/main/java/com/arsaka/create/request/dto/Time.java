package com.arsaka.create.request.dto;

import com.arsaka.validation.annotation.ValidScheduledTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@ValidScheduledTime
public record Time(
        @NotNull
        @FutureOrPresent
        Instant departure,

        @FutureOrPresent
        Instant arrival
) {
}
