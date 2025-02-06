package org.univartois.exception.handler;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
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

    @ServerExceptionMapper
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


    @ServerExceptionMapper
    public RestResponse<ApiResponse<Object>> handleOtherException(RuntimeException exception) {
        log.error("Unhandled exception at {}: {}", uriInfo.getPath(), exception.getMessage(), exception);
        ApiResponse<Object> response;
        if (exception instanceof WebApplicationException webApplicationException) {
            response = ResponseUtil.errorFromStrings(
              new ArrayList<String>(),
              webApplicationException.getMessage(),
                    RestResponse.Status.fromStatusCode(webApplicationException.getResponse().getStatus()),
              uriInfo.getPath()
            );
        } else {

            response = ResponseUtil.errorFromStrings(
                    new ArrayList<String>(),
                    "An unexpected error occurred",
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    uriInfo.getPath()

            );
        }
        return RestResponse.status(response.getStatusCode(), response);
    }


}
