package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.config.security.model.UserPrincipal;
import com.capibaracode.backend.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserPrincipal userPrincipalFromUser(User user);
}
