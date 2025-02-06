package org.univartois.exception;

import jakarta.ws.rs.BadRequestException;

public class UserAlreadyExistsException extends  BadRequestException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
