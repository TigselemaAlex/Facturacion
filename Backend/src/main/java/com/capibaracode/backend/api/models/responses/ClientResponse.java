package com.capibaracode.backend.api.models.responses;

import com.capibaracode.backend.util.enums.ClientType;
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
    private Boolean active;
    private String address;
    private ClientType type;
}
