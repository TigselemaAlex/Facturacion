package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.TaxRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ITaxService {

    ResponseEntity<CustomAPIResponse<?>> save(TaxRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, TaxRequest request);
    ResponseEntity<CustomAPIResponse<?>> findByID(UUID id);
    ResponseEntity<CustomAPIResponse<?>> changeStatus(UUID id);

}
