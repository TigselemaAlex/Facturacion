package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.TaxRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.ITaxService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/tax")
@SecurityRequirement(name = "swagger")
//http://localhost:8090/capy-bills/api/protected/tax
public class TaxController {

    private final ITaxService taxService;

    public TaxController(ITaxService taxService) {
        this.taxService = taxService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save (@Valid @RequestBody final TaxRequest request){
        return taxService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return taxService.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> getByID(@PathVariable final UUID id){
        return taxService.findByID(id);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final TaxRequest request){
        return taxService.update(id, request);
    }

}
