package com.capibaracode.backend.common;

import com.capibaracode.backend.api.models.responses.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomResponseBuilder {

    public ResponseEntity<CustomAPIResponse<?>> buildResponse(HttpStatus httpStatus, String message){
        return new CustomAPIResponse.APIResponseBuilder<>(httpStatus, message).build();
    }
    public ResponseEntity<CustomAPIResponse<?>> buildResponse(HttpStatus httpStatus, String message, Object data){
        return new CustomAPIResponse.APIResponseBuilder<>(httpStatus, message).data(data).build();
    }
}
