package org.univartois.exception;

import jakarta.ws.rs.BadRequestException;

public class TokenInvalidException extends BadRequestException {
    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
