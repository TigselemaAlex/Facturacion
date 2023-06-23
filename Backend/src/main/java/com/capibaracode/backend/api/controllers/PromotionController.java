package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.PromotionRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IPromotionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/promotion")
@SecurityRequirement(name = "swagger")
public class PromotionController {

    private final IPromotionService promotionService;


    public PromotionController(IPromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save(@Valid @RequestBody final PromotionRequest request){
        return promotionService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return promotionService.getAll();
    }

    @GetMapping(value = "/{description}")
    public ResponseEntity<CustomAPIResponse<?>> getByDescription(@PathVariable final String description){
        return promotionService.findByDescriptionName(description);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final PromotionRequest request){
        return promotionService.update(id, request);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> delete(@PathVariable final UUID id){
        return promotionService.delete(id);
    }

}
