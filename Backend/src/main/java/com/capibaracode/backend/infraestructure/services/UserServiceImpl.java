package com.capibaracode.backend.infraestructure.services;


import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.ResponseBuilder;
import com.capibaracode.backend.domain.entities.Company;
import com.capibaracode.backend.domain.entities.User;
import com.capibaracode.backend.domain.repositories.CompanyRepository;
import com.capibaracode.backend.domain.repositories.UserRepository;
import com.capibaracode.backend.infraestructure.abstract_services.IUserService;
import com.capibaracode.backend.util.enums.Role;
import com.capibaracode.backend.util.mappers.CompanyMapper;
import com.capibaracode.backend.util.mappers.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final ResponseBuilder responseBuilder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate, ResponseBuilder responseBuilder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(RegisterRequest request) {
        Company company = CompanyMapper.INSTANCE.companyFromRegisterRequest(request);
        User adminUser = new User();
        adminUser.setActive(true);
        adminUser.setCompany(company);
        adminUser.setRole(Role.ADMINISTRADOR);
        adminUser.setIdentification(company.getRuc());
        adminUser.setFullName(company.getName().concat("_admin"));
        adminUser.setUsername(company.getName().concat("_admin"));
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setTelephone(company.getPhone());
        adminUser.setEmail(company.getEmail());
        company.addUser(adminUser);
        Company companyFromDB = companyRepository.save(company);
        String schemaName = companyFromDB.getName();

        Flyway flyway = Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(jdbcTemplate.getDataSource())
                .schemas(schemaName)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();

        return responseBuilder.buildResponse(HttpStatus.CREATED, "Compania registrada exitosamente");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        return UserMapper.INSTANCE.userPrincipalFromUser(user);
    }
}
