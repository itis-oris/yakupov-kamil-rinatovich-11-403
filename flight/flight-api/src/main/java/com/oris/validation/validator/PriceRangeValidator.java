package com.oris.validation.validator;

import com.oris.search.request.dto.PriceRange;
import com.oris.validation.annotation.ValidPriceRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, PriceRange> {

    @Override
    public boolean isValid(PriceRange value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        BigDecimal begin = value.begin();
        BigDecimal end = value.end();


        if (begin == null || end == null) {
            return true;
        }

        return begin.compareTo(end) <= 0;
    }
}