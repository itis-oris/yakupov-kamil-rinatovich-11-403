package com.arsaka.search.request.dto;

import com.arsaka.validation.annotation.ValidPriceRange;
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
