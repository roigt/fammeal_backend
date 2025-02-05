package org.univartois.utils;

import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.response.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class ResponseUtil {
    public static <T> ApiResponse<T> success(T data, String message, RestResponse.Status status, String path) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(status)
                .path(path)
                .timestamp(System.currentTimeMillis())
                .data(data)
                .errors(new ArrayList<>())
                .build();
    }


    public static <T> ApiResponse<T> errorFromStrings(List<String> errors, String message, RestResponse.Status status, String path) {
        List<ApiResponse.Error> errorList = errors.stream()
                .map(ApiResponse.Error::new)
                .toList();

        return buildErrorResponse(errorList, message, status, path);
    }

    public static <T> ApiResponse<T> errorFromApiErrors(List<ApiResponse.Error> errors, String message, RestResponse.Status status, String path) {
        return buildErrorResponse(new ArrayList<>(errors), message, status, path);
    }

    private static <T> ApiResponse<T> buildErrorResponse(List<ApiResponse.Error> errors, String message, RestResponse.Status status, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .statusCode(status)
                .path(path)
                .timestamp(System.currentTimeMillis())
                .data(null)
                .errors(errors)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage, String errorField, String message, RestResponse.Status status, String path) {
        List<ApiResponse.Error> errors = new ArrayList<>();
        errors.add(ApiResponse.createError(errorMessage, errorField));
        return buildErrorResponse(errors, message, status, path);
    }


}
