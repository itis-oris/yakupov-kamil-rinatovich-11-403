package com.arsaka.auth.util;

import com.arsaka.auth.common.AccountRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

//@Converter(autoApply = true)
public class AccountRoleConverter implements AttributeConverter<AccountRole, String> {

    @Override
    public String convertToDatabaseColumn(AccountRole role) {
        return role.getValue();
    }

    @Override
    public AccountRole convertToEntityAttribute(String value) {

        return Arrays.stream(AccountRole.values())
                .filter(r -> r.getValue().equals(value))
                .findFirst()
                .orElseThrow();
    }
}