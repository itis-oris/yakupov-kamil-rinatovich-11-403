package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class CountryNotFoundException extends NotFoundException {
    public CountryNotFoundException(String code) {
        super("Country not found | country code=%s".formatted(code));
    }
}
