package com.oris.validation.annotation;

import com.oris.validation.validator.PriceRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceRangeValidator.class)
public @interface ValidPriceRange {

    String message() default "price 'begin' must be <= 'end'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}