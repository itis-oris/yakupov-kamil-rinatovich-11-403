package com.arsaka.search.request.dto;

import com.arsaka.validation.annotation.ValidScheduledDate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidScheduledDate
public record Date(
        @NotNull
        @FutureOrPresent
        LocalDate departure,

        @FutureOrPresent
        LocalDate arrival
) {
}
