package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.api.models.requests.UserRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IUserService {
     ResponseEntity<CustomAPIResponse<?>> register(RegisterRequest request);

     ResponseEntity<CustomAPIResponse<?>> updatePassword(UUID userId, String password);

     ResponseEntity<CustomAPIResponse<?>> update(UUID companyId, UserRequest request);

     ResponseEntity<CustomAPIResponse<?>> save(UUID companyId, UserRequest request);

}
