package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.infraestructure.abstract_services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "public/auth")
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/logup")
    public ResponseEntity<CustomAPIResponse<?>> logUp(@RequestBody final RegisterRequest request){
        return userService.save(request);
    }
}
