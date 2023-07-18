package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.InvoiceRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IInvoiceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/invoices")
@SecurityRequirement(name = "swagger")
public class InvoiceController {

    private final IInvoiceService invoiceService;

    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> create(@RequestBody final InvoiceRequest request){
        return invoiceService.createInvoice(request);
    }

    @GetMapping()
    public ResponseEntity<CustomAPIResponse<?>> findAll(){
        return invoiceService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> findById(@PathVariable final UUID id){
        return invoiceService.findById(id);
    }
}
