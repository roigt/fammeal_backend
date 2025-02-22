package org.univartois.exception;

import jakarta.ws.rs.ForbiddenException;

public class AdminRoleModificationException extends ForbiddenException {
    public AdminRoleModificationException(String message) {
        super(message);
    }

    public AdminRoleModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
