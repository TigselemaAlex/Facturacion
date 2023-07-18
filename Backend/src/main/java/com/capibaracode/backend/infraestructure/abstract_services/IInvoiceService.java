package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.InvoiceRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IInvoiceService {
    ResponseEntity<CustomAPIResponse<?>> createInvoice(InvoiceRequest request);

    ResponseEntity<CustomAPIResponse<?>> findAll();

    ResponseEntity<CustomAPIResponse<?>> findById(UUID id);

}
