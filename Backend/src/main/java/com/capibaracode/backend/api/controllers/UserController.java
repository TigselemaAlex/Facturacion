package com.capibaracode.backend.api.controllers;

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

    @PatchMapping(value = "/recovery/password/{email}")
    public ResponseEntity<CustomAPIResponse<?>> recoveryPassword(@PathVariable("email") String email){
        return userService.recoveryPassword(email);
    }


}
