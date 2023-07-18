package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.ProductRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/product")
@SecurityRequirement(name = "swagger")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save(@Valid @RequestBody final ProductRequest request){
        return productService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return productService.getAll();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final ProductRequest request){
        return productService.update(id, request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> findByID(@PathVariable final UUID id){
        return productService.findByID(id);
    }

    @GetMapping(value = "/by-supplier/{id}")
    public ResponseEntity<CustomAPIResponse<?>> findByIDSupplier(@PathVariable final UUID id){
        return productService.findByIDSupplier(id);
    }

}
