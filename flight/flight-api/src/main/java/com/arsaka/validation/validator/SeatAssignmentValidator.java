package com.arsaka.validation.validator;

import com.arsaka.common.PassengerType;
import com.arsaka.event.dto.FlightHold;
import com.arsaka.validation.annotation.ValidSeatAssignment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SeatAssignmentValidator implements ConstraintValidator<ValidSeatAssignment, FlightHold> {

    @Override
    public boolean isValid(FlightHold value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.passengerType() == null) {
            return true;
        }

        if (value.passengerType() == PassengerType.INFANT) {
            return true;
        }

        if (value.seatId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("seatId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}