package com.capibaracode.backend.infraestructure.services;


import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.api.models.requests.UserRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.config.security.model.UserPrincipal;
import com.capibaracode.backend.domain.entities.Company;
import com.capibaracode.backend.domain.entities.User;
import com.capibaracode.backend.domain.repositories.CompanyRepository;
import com.capibaracode.backend.domain.repositories.UserRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IEmailService;
import com.capibaracode.backend.infraestructure.abstract_services.IUserService;
import com.capibaracode.backend.util.Email.EmailDetails;
import com.capibaracode.backend.util.RegisterUtils;
import com.capibaracode.backend.util.enums.Role;
import com.capibaracode.backend.util.mappers.CompanyMapper;
import com.capibaracode.backend.util.mappers.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomResponseBuilder responseBuilder;
    private  final IEmailService emailService;
    private final RegisterUtils registerUtils;

    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, CustomResponseBuilder responseBuilder, IEmailService emailService, RegisterUtils registerUtils) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerUtils = registerUtils;
        this.responseBuilder = responseBuilder;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> register(RegisterRequest request) {
        if(companyRepository.existsByName(request.getName())){
            return responseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "Ya existe una compania registrada con ese nombre");
        }
        Company company = CompanyMapper.INSTANCE.companyFromRegisterRequest(request);
        User adminUser = new User();
        adminUser.setStatus(true);
        adminUser.setCompany(company);
        adminUser.setRole(Role.ADMINISTRADOR);
        adminUser.setIdentification(company.getRuc());
        adminUser.setFullName(company.getName().concat("_admin"));
        String password = registerUtils.generatePassword();
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setTelephone(company.getPhone());
        adminUser.setEmail(company.getEmail());
        company.addUser(adminUser);
        Company companyFromDB = companyRepository.save(company);
        String schemaName = companyFromDB.getName().replaceAll("\\s+", "");
        registerUtils.createSchema(schemaName);
        emailService.sendSimpleMail(
                new EmailDetails(adminUser.getEmail(), registerUtils.bodyMessage(password), "Generación de contraseña"));

        return responseBuilder.buildResponse(HttpStatus.CREATED, "Compania registrada exitosamente");
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> updatePassword(UUID companyId, String password) {
        return null;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID companyId, UserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(UUID companyId, UserRequest request) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        UserPrincipal userPrincipal = UserMapper.INSTANCE.userPrincipalFromUser(user);
        userPrincipal.setAuthorities(UserMapper.INSTANCE.mapRolesToAuthorities(user.getRole()));
        return userPrincipal;
    }





}
