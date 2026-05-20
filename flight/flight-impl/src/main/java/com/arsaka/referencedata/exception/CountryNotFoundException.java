package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class CountryNotFoundException extends NotFoundException {
    public CountryNotFoundException() {
        super("Country not found");
    }
}
