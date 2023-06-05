package com.capibaracode.backend.infraestructure.services;


import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.domain.repositories.CompanyRepository;
import com.capibaracode.backend.domain.repositories.UserRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(RegisterRequest request) {

        return null;
    }
}
