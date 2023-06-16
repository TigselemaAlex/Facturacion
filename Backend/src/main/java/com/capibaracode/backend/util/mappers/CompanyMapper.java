package com.capibaracode.backend.util.mappers;

import com.capibaracode.backend.api.models.requests.RegisterRequest;
import com.capibaracode.backend.domain.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);
    Company companyFromRegisterRequest(RegisterRequest request);
}
