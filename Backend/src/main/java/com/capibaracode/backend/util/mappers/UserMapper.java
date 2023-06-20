package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.responses.CompanyResponse;
import com.capibaracode.backend.api.models.responses.UserResponse;
import com.capibaracode.backend.config.security.model.UserPrincipal;
import com.capibaracode.backend.domain.entities.User;
import com.capibaracode.backend.util.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "tenant", expression = "java(user.getCompany().getName().replaceAll(\"\\\\s+\", \"\"))")
    UserPrincipal userPrincipalFromUser(User user);

    default List<SimpleGrantedAuthority> mapRolesToAuthorities(Role rol) {
        if (rol == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(rol.name()));
    }

    @Mapping(target = "company", source = "companyResponse")
    @Mapping(target = "id", source = "userPrincipal.id")
    @Mapping(target = "email", source = "userPrincipal.email")
    UserResponse userResponseFromUserPrincipal(UserPrincipal userPrincipal, CompanyResponse companyResponse);
}
