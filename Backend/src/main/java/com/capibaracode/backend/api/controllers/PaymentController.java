package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.PaymentRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IPaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/payment")
@SecurityRequirement(name = "swagger")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save(@Valid @RequestBody final PaymentRequest request){
        return paymentService.save(request);
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> getAll(){
        return paymentService.getAll();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final PaymentRequest request){
        return paymentService.update(id,request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id){
        return paymentService.getByID(id);
    }

    @PatchMapping(value = "/change-status/{id}")
    public ResponseEntity<CustomAPIResponse<?>> changeStatus(@PathVariable final UUID id){
        return paymentService.changeStatus(id);
    }

}
