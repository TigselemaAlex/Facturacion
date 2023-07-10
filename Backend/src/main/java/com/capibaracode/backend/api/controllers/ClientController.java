package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.ClientRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IClientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/protected/clients")
@SecurityRequirement(name = "swagger")
public class ClientController {

    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<CustomAPIResponse<?>> findAll(){
        return clientService.findAll();
    }
    @GetMapping(value = "/only-active")
    public ResponseEntity<CustomAPIResponse<?>> findAllOnlyActive(){
        return clientService.findAllOnlyActive();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> findById(@PathVariable final UUID id){
        return clientService.findById(id);
    }

    @GetMapping(value = "/identification/{identification}")
    public ResponseEntity<CustomAPIResponse<?>> findByIdentification(@PathVariable final String identification){
        return clientService.findByIdentification(identification);
    }

    @PostMapping
    public ResponseEntity<CustomAPIResponse<?>> save(@Valid @RequestBody final ClientRequest request){
        return clientService.save(request);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable final UUID id, @Valid @RequestBody final ClientRequest request){
        return clientService.update(id, request);
    }


}
