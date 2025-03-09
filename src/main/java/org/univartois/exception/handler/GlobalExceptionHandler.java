package org.univartois.exception.handler;


import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.univartois.dto.response.ApiResponse;
import org.univartois.utils.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GlobalExceptionHandler {

    @Context
    UriInfo uriInfo;

    @ServerExceptionMapper(value = {ValidationException.class})
    public RestResponse<ApiResponse<Object>> handleValidationException(ValidationException exception) {
//        String message = exception.getMessage();
        String message = "Validation failed";
        List<ApiResponse.Error> errors = new ArrayList<>();

        if (exception instanceof ConstraintViolationException constraintViolationException) {
            errors = constraintViolationException.getConstraintViolations()
                    .stream().map(constraintViolation -> new ApiResponse.Error(
                                    constraintViolation.getMessage(),
                                    extractFieldName(constraintViolation.getPropertyPath().toString())
                            )
                    ).toList();
        }
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, ResponseUtil.errorFromApiErrors(errors, message, RestResponse.Status.BAD_REQUEST, uriInfo.getPath()));
    }


    private String extractFieldName(String fullPath) {
        String[] parts = fullPath.split("\\.");
        return parts.length >= 1 ? parts[parts.length - 1] : "";
    }


    @ServerExceptionMapper(value = {UnauthorizedException.class, ForbiddenException.class, AuthenticationFailedException.class, SecurityException.class}, priority = Priorities.AUTHENTICATION - 1)
    public RestResponse<ApiResponse<Object>> handleSecurityException(SecurityException exception) {
        String errorMessage = exception.getMessage();
        errorMessage = (errorMessage == null || errorMessage.isBlank()) ? "Veuillez vous authentifier avant d'accéder à cette resource ." : errorMessage;

        log.error("security exception at {}: {}", uriInfo.getPath(), errorMessage, exception);

        ApiResponse<Object> response;
        response = ResponseUtil.errorFromStrings(
                new ArrayList<>(),
                errorMessage,
                RestResponse.Status.fromStatusCode(401),
                uriInfo.getPath()
        );
        if (exception instanceof ForbiddenException forbiddenException) {
            response.setStatusCode(RestResponse.Status.FORBIDDEN.getStatusCode());
        }
        return RestResponse.status(RestResponse.Status.fromStatusCode(response.getStatusCode()), response);
    }

    @ServerExceptionMapper(value = {RuntimeException.class})
    public RestResponse<ApiResponse<Object>> handleOtherException(RuntimeException exception) {
        log.error("Unhandled exception at {}: {}", uriInfo.getPath(), exception.getMessage(), exception);
        ApiResponse<Object> response;
        if (exception instanceof WebApplicationException webApplicationException) {
            response = ResponseUtil.errorFromStrings(
                    new ArrayList<>(),
                    webApplicationException.getMessage(),
                    RestResponse.Status.fromStatusCode(webApplicationException.getResponse().getStatus()),
                    uriInfo.getPath()
            );
        } else {

            response = ResponseUtil.errorFromStrings(
                    new ArrayList<>(),
                    "An unexpected error occurred",
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    uriInfo.getPath()

            );
        }
        return RestResponse.status(RestResponse.Status.fromStatusCode(response.getStatusCode()), response);
    }


}
