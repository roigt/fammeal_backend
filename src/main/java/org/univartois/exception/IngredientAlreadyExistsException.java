package org.univartois.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class IngredientAlreadyExistsException extends WebApplicationException {

  public IngredientAlreadyExistsException(final String message) {
    super(message, Response.Status.CONFLICT);
  }

  public IngredientAlreadyExistsException(final String message, final Throwable cause) {
    super(message, cause, Response.Status.CONFLICT);
  }

}
