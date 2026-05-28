package com.oris.auth.exception;

public class NoSuchDefaultAccountRoleException extends ServiceException {
    public NoSuchDefaultAccountRoleException() {
        super("No such default role");
    }
}
