package org.univartois.exception;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class CannotLeaveHomeException extends ForbiddenException {
    public CannotLeaveHomeException(String message) {
        super(message);
    }

    public CannotLeaveHomeException(String message, Throwable cause) {
        super(message, cause);
    }
}
