package org.univartois.exception;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class UserAlreadyInHomeException extends WebApplicationException {
    public UserAlreadyInHomeException(String message) {
        super(message, Response.Status.CONFLICT);
    }

    public UserAlreadyInHomeException(String message, Throwable cause) {
        super(message, cause, Response.Status.CONFLICT);
    }
}
