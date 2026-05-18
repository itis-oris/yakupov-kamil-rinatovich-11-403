package com.arsaka.validation.annotation;

import com.arsaka.validation.validator.SeatAssignmentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SeatAssignmentValidator.class)
public @interface ValidSeatAssignment {

    String message() default "seat id must not be null for this type of passenger";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}