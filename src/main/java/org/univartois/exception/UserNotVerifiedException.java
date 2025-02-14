package org.univartois.exception;

import jakarta.ws.rs.ForbiddenException;

public class UserNotVerifiedException extends ForbiddenException {
    public UserNotVerifiedException(String message) {
        super(message);
    }

    public UserNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
