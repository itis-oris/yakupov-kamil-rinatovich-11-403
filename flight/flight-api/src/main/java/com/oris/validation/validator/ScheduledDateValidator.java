package com.oris.validation.validator;

import com.oris.search.request.dto.Date;
import com.oris.validation.annotation.ValidScheduledDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ScheduledDateValidator implements ConstraintValidator<ValidScheduledDate, Date> {

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate departure = value.departure();
        LocalDate arrival = value.arrival();


        if (departure == null || arrival == null) {
            return true;
        }

        return !departure.isAfter(arrival);
    }
}