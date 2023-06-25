package com.capibaracode.backend.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SupplierResponse {

    private UUID id;

    private String identification;

    private String name;

    private String email;

    private String address;

    private String telephone;

    private Boolean status;

}
