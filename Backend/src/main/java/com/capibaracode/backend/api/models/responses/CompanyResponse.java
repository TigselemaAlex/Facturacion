package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.util.enums.CompanyType;
import com.capibaracode.backend.util.enums.EnvironmentType;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String name,
        String email,
        String ruc,
        String phone,
        String address,
        String logo,
        Boolean accounting,
        CompanyType type,
        EnvironmentType environment
) {
}
