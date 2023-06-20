package com.capibaracode.backend.config.security.model;

import com.capibaracode.backend.domain.entities.Company;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class UserPrincipal implements UserDetails {
    private UUID id;
    private String email;
    private String fullName;
    private String password;
    private String tenant;
    private Boolean status;
    private String identification;
    private String telephone;
    private Company company;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}
