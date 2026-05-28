package com.oris.validation.validator;

import com.oris.create.request.dto.Time;
import com.oris.validation.annotation.ValidScheduledTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class ScheduledTimeValidator implements ConstraintValidator<ValidScheduledTime, Time> {

    @Override
    public boolean isValid(Time value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Instant departure = value.departure();
        Instant arrival = value.arrival();


        if (departure == null || arrival == null) {
            return true;
        }

        return !departure.isAfter(arrival);
    }
}