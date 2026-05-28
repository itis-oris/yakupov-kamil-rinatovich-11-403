package com.oris.referencedata.exception;

import com.oris.exception.NotFoundException;

public class CountryNotFoundException extends NotFoundException {
    public CountryNotFoundException() {
        super("Country not found");
    }
}
