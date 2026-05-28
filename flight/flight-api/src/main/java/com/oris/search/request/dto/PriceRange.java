package com.oris.search.request.dto;

import com.oris.validation.annotation.ValidPriceRange;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@ValidPriceRange
public record PriceRange(
        @PositiveOrZero
        BigDecimal begin,

        @PositiveOrZero
        BigDecimal end
) {
}
