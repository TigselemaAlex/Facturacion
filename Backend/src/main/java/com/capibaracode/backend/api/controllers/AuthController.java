package com.capibaracode.backend.api.controllers;

import com.capibaracode.backend.api.models.requests.LoginRequest;
import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.api.models.responses.JWTResponse;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.config.security.jwt.JWTProvider;
import com.capibaracode.backend.config.security.model.UserPrincipal;
import com.capibaracode.backend.infraestructure.abstract_services.IUserService;
import com.capibaracode.backend.util.mappers.CompanyMapper;
import com.capibaracode.backend.util.mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "public/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final IUserService userService;
    private final CustomResponseBuilder responseBuilder;

    public AuthController(AuthenticationManager authenticationManager, JWTProvider jwtProvider, IUserService userService, CustomResponseBuilder responseBuilder) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping(value = "/logup")
    public ResponseEntity<CustomAPIResponse<?>> logUp(@RequestBody final RegisterRequest request){
        return userService.register(request);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<CustomAPIResponse<?>> logIn(@RequestBody final LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        JWTResponse response = new JWTResponse(jwt,userPrincipal.getTenant(), UserMapper.INSTANCE.userResponseFromUserPrincipal(userPrincipal,
                CompanyMapper.INSTANCE.companyResponseFromCompany(userPrincipal.getCompany())));
        return responseBuilder.buildResponse(HttpStatus.OK, "Usuario logeado exitosamente", response);
    }
}
