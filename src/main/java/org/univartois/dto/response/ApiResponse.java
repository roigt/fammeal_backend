package org.univartois.dto.response;

import lombok.*;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ApiResponse<T> {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class Error {
        private String message;
        private String field;

        public Error(String message){
            this.message = message;
        }
    }

    private boolean success;
    private String message;
    private T data;
    private List<Error> errors;
    private RestResponse.Status statusCode;
    private long timestamp;
    private String path;

    public static Error createError(String message, String field){
        return new Error(message, field);
    }

    public static Error createError(String message){
        return new Error(message);
    }

}
