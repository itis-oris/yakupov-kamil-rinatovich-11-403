package com.arsaka.validation.annotation;

import com.arsaka.validation.validator.ScheduledDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ScheduledDateValidator.class)
public @interface ValidScheduledDate {

    String message() default "date 'departure' must be <= 'arrival'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}