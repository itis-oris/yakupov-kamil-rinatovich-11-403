package com.arsaka.search.request;

import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
import com.arsaka.search.request.dto.FlightsSearchFilter;
import com.arsaka.search.request.dto.Date;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Map;

public record FlightsSearchRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String cityCodeFrom,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String cityCodeTo,

        @NotNull
        @Valid
        Date scheduledDate,

        @NotEmpty
        Map<@NotNull PassengerType, @Positive Integer> passengers,

        @NotNull
        CabinClass cabinClass,

        @Valid
        FlightsSearchFilter filter,

        String cursor
) {
}
