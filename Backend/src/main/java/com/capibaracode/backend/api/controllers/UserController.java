package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.UserRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/protected/users")
@SecurityRequirement(name = "swagger")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PatchMapping(value = "/update/password/{id}/{password}")
    public ResponseEntity<CustomAPIResponse<?>> changePassword(@PathVariable("id") UUID id, @PathVariable("password") String password){
        return userService.updatePassword(id, password);
    }

    @PostMapping(value = "/create/{companyId}")
    public ResponseEntity<CustomAPIResponse<?>> create(@RequestBody UserRequest request, @PathVariable("companyId") UUID companyId){
        return userService.save(request, companyId);
    }

    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<CustomAPIResponse<?>> update(@PathVariable("id") UUID id, @RequestBody UserRequest request){
        return userService.update(id, request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomAPIResponse<?>> findById(@PathVariable("id") UUID id){
        return userService.findById(id);
    }

    @GetMapping(value = "/company/{companyId}")
    public ResponseEntity<CustomAPIResponse<?>> findAllByCompany(@PathVariable("companyId") UUID companyId){
        return userService.findAllByCompany(companyId);
    }

}
