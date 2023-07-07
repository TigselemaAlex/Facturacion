package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.PaymentRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPaymentService {

    ResponseEntity<CustomAPIResponse<?>> save(PaymentRequest request);
    ResponseEntity<CustomAPIResponse<?>> getAll();
    ResponseEntity<CustomAPIResponse<?>> update(UUID id, PaymentRequest request);
    ResponseEntity<CustomAPIResponse<?>> getByID(UUID id);

}
