package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.util.enums.IdentificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClientResponse {
    private UUID id;
    private String identification;
    private String fullname;
    private String telephone;
    private String email;
    private Boolean status;
    private String address;

    private IdentificationType identificationType;
}
