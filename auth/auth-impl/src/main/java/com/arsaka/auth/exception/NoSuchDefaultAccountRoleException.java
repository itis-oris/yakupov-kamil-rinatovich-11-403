package com.arsaka.auth.exception;

import com.arsaka.auth.common.AccountRole;

public class NoSuchDefaultAccountRoleException extends ServiceException {
    public NoSuchDefaultAccountRoleException(AccountRole role) {
        super("No such default %s roles".formatted(role));
    }
}
