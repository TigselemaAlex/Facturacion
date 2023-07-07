package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.PurchaseDetailRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPurchaseDetailService {

    ResponseEntity<CustomAPIResponse<?>> save(PurchaseDetailRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, PurchaseDetailRequest request);
    ResponseEntity<CustomAPIResponse<?>> getByIdPurchase(UUID id);
    ResponseEntity<CustomAPIResponse<?>> changeStatus(UUID id);

}
