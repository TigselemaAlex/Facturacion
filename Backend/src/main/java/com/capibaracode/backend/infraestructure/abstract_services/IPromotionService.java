package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.PromotionRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPromotionService {

    ResponseEntity<CustomAPIResponse<?>> save(PromotionRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, PromotionRequest request);
    ResponseEntity<CustomAPIResponse<?>> findByID(UUID id);
    ResponseEntity<CustomAPIResponse<?>> delete(UUID id);


}
