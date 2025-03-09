package org.univartois.exception;

import jakarta.ws.rs.BadRequestException;

public class OldPasswordMismatchException extends BadRequestException {

    public OldPasswordMismatchException(String message) {
        super(message);
    }

    public OldPasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
