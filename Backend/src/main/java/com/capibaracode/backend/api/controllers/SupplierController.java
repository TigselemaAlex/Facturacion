package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.SupplierRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.ISupplierService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/supplier")
@SecurityRequirement(name = "swagger")
public class SupplierController {

    private final ISupplierService supplierService;

    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save(@Valid @RequestBody final SupplierRequest request){
        return supplierService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return supplierService.getAll();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final SupplierRequest request){
        return supplierService.update(id, request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> getByID(@PathVariable final UUID id){
        return supplierService.getByID(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> delete(@PathVariable final UUID id){
        return supplierService.delete(id);
    }

}
