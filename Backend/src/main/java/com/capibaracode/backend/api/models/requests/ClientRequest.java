package com.capibaracode.backend.api.models.requests;

import com.capibaracode.backend.util.enums.ClientType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientRequest {
    private String identification;
    private String fullname;
    private String telephone;
    private String email;
    private String address;
    private ClientType type;
}
