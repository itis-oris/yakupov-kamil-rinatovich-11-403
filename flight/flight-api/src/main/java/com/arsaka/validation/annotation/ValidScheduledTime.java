package com.arsaka.validation.annotation;

import com.arsaka.validation.validator.ScheduledTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ScheduledTimeValidator.class)
public @interface ValidScheduledTime {

    String message() default "time 'departure' must be <= 'arrival'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}