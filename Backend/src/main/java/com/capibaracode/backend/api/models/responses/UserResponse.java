package com.capibaracode.backend.api.models.responses;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        Boolean status,
        String identification,
        String telephone,
        Collection<? extends GrantedAuthority> authorities,
        CompanyResponse company) {
}
