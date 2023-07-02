package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.PurchaseRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.Purchase;
import com.capibaracode.backend.domain.repositories.PurchaseRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IPurchaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class PurchaseServiceImpl implements IPurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, CustomResponseBuilder responseBuilder) {
        this.purchaseRepository = purchaseRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(PurchaseRequest request) {

        return null;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, PurchaseRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> changeStatus(UUID id) {
        if (purchaseRepository.existsById(id)){
            Purchase purchase = purchaseRepository.findById(id).orElseThrow(()-> new RuntimeException("La compra con id " + id + " no existe."));
            boolean statusValue;
            if (purchase.getStatus()){
                purchase.setStatus(false);
            }else{
                purchase.setStatus(true);
            }
            statusValue = purchase.getStatus();
            return responseBuilder.buildResponse(HttpStatus.OK, "Cambio de estado exitosamente.", statusValue);
        }
        throw  new RuntimeException("La compra con el identificador: " + id + " no se encuentra.");

    }
}
