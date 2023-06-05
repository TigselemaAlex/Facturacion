package com.capibaracode.backend.infraestructure.abstract_services;

import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

public interface IUserService {
     ResponseEntity<CustomAPIResponse<?>> save(RegisterRequest request);
}
