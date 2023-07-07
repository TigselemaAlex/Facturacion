package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.PurchaseRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPurchaseService {

    public ResponseEntity<CustomAPIResponse<?>> save(PurchaseRequest request);
    public ResponseEntity<CustomAPIResponse<?>> getAll();
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, PurchaseRequest request);
    public ResponseEntity<CustomAPIResponse<?>> getByID(UUID id);

}
