package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.SupplierRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ISupplierService {

    ResponseEntity<CustomAPIResponse<?>> save(SupplierRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, SupplierRequest request);
    ResponseEntity<CustomAPIResponse<?>> getByID(UUID id);
    ResponseEntity<CustomAPIResponse<?>> delete(UUID id);

}
