package org.univartois.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class UserAlreadyExistsException extends WebApplicationException {

    public UserAlreadyExistsException(final String message) {
        super(message, Response.Status.CONFLICT);
    }

    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause, Response.Status.CONFLICT);
    }

}
