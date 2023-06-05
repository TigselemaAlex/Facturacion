package com.capibaracode.backend.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"httpStatus", "message", "data"})
public class CustomAPIResponse <T>{
    private final HttpStatus httpStatus;

    private final String message;

    private final T data;

    private CustomAPIResponse(APIResponseBuilder<T> apiResponseBuilder){
        this.httpStatus = apiResponseBuilder.httpStatus;
        this.message = apiResponseBuilder.message;
        this.data = apiResponseBuilder.data;
    }

    public static class APIResponseBuilder<T>{
        private final HttpStatus httpStatus;

        private final String message;

        private T data;

        public  APIResponseBuilder(HttpStatus httpStatus, String message){
            this.httpStatus = httpStatus;
            this.message = message;
        }

        public APIResponseBuilder<T> data (T data){
            this.data = data;
            return this;
        }

        public ResponseEntity<CustomAPIResponse<?>> build(){
            CustomAPIResponse<T> response = new CustomAPIResponse<>(this);
            return new ResponseEntity<>(response, response.getHttpStatus());
        }
    }
}
