package com.capibaracode.backend.api.controllers;


import com.capibaracode.backend.api.models.requests.PurchaseDetailRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IPurchaseDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/protected/purchase-detail")
@SecurityRequirement(name = "swagger")
public class PurchaseDetailController {

    private final IPurchaseDetailService purchaseDetailService;

    public PurchaseDetailController(IPurchaseDetailService purchaseDetailService) {
        this.purchaseDetailService = purchaseDetailService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save (@Valid @RequestBody final PurchaseDetailRequest request){
        return purchaseDetailService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return purchaseDetailService.getAll();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final PurchaseDetailRequest request){
        return purchaseDetailService.update(id, request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> getByIdPurchase(@PathVariable final UUID id){
        return purchaseDetailService.getByIdPurchase(id);
    }

    @PatchMapping(value = "/change-status/{id}")
    public ResponseEntity<CustomAPIResponse<?>> changeStatus(@PathVariable final UUID id){
        return purchaseDetailService.changeStatus(id);
    }

}
