package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.CategoryRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ICategoryService {

    ResponseEntity<CustomAPIResponse<?>> save(CategoryRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, CategoryRequest request);
    ResponseEntity<CustomAPIResponse<?>> findByID(UUID id);

}
