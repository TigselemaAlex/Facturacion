package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.ProductRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IProductService {

    ResponseEntity<CustomAPIResponse<?>> save(ProductRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, ProductRequest request);
    ResponseEntity<CustomAPIResponse<?>> findByID(UUID id);
    ResponseEntity<CustomAPIResponse<?>> delete(UUID id);

}
